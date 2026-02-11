package com.example.techgicus_ebilling.techgicus_ebilling.imports.processor;

import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.*;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.ItemType;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.StockTransactionType;
import com.example.techgicus_ebilling.techgicus_ebilling.imports.context.ImportContext;
import com.example.techgicus_ebilling.techgicus_ebilling.imports.dto.PurchaseItemRow;
import com.example.techgicus_ebilling.techgicus_ebilling.imports.extractor.PurchaseItemRowExtractor;
import com.example.techgicus_ebilling.techgicus_ebilling.imports.mapper.PurchaseItemEntityMapper;
import com.example.techgicus_ebilling.techgicus_ebilling.imports.utill.ModelUtill;
import com.example.techgicus_ebilling.techgicus_ebilling.repository.*;
import org.apache.poi.ss.usermodel.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

@Service
public class PurchaseItemTransactionProcessor implements TransactionProcessor{

    private final PurchaseItemRowExtractor purchaseItemRowExtractor;
    private final PurchaseRepository purchaseRepository;
    private final PurchaseItemRepository purchaseItemRepository;
    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;
    private final PurchaseItemEntityMapper purchaseItemEntityMapper;
    private final PurchaseBatchRepository purchaseBatchRepository;
    private final StockTransactionRepository stockTransactionRepository;

    private static final Logger log =
            LoggerFactory.getLogger(PurchaseItemTransactionProcessor.class);


    public PurchaseItemTransactionProcessor(PurchaseItemRowExtractor purchaseItemRowExtractor, PurchaseRepository purchaseRepository, PurchaseItemRepository purchaseItemRepository, ItemRepository itemRepository, CategoryRepository categoryRepository, PurchaseItemEntityMapper purchaseItemEntityMapper, PurchaseBatchRepository purchaseBatchRepository, StockTransactionRepository stockTransactionRepository) {
        this.purchaseItemRowExtractor = purchaseItemRowExtractor;
        this.purchaseRepository = purchaseRepository;
        this.purchaseItemRepository = purchaseItemRepository;
        this.itemRepository = itemRepository;
        this.categoryRepository = categoryRepository;
        this.purchaseItemEntityMapper = purchaseItemEntityMapper;
        this.purchaseBatchRepository = purchaseBatchRepository;
        this.stockTransactionRepository = stockTransactionRepository;
    }

    @Override
    public boolean supports(String transactionType) {
        if (transactionType.toLowerCase().trim().equals("purchase-item"))return true;
        return false;
    }

    @Override
    public void process(Row row, Company company, ImportContext context) {

        PurchaseItemRow purchaseItemRow = purchaseItemRowExtractor.extract(row);

        Optional<Purchase> purchase = purchaseRepository.findByBillNumberAndCompany(purchaseItemRow.getBillNo(),company);

        if (purchase.isEmpty()){
            if (purchaseItemRow.getBillNo() == null || purchaseItemRow.getBillNo().isEmpty()){
                context.addError(row.getSheet().getSheetName(),row.getRowNum()+1,"Invoice number is missing. Purchase items require an existing Purchase Invoice.");
                return;
            }
            context.addError(row.getSheet().getSheetName(),row.getRowNum()+1,"Purchase not found for invoice : "+purchaseItemRow.getBillNo());
            return;
        }

        Item item = ModelUtill.findOrCreateItem(purchaseItemRow,
                company,
                itemRepository,
                categoryRepository);


        // 🔥 CLEAR ONLY ONCE PER SALE
        if (!context.isInitialized(purchase.get())) {

            log.info("Re-import detected for purchase {}, capturing old quantities",
                    purchase.get().getBillNumber());

            /*
             * 1️⃣ Capture OLD Quantities (CRITICAL)
             */
            Map<Long, Double> oldQtyMap =
                    purchaseItemRepository.sumQuantityGroupByItem(
                            purchase.get().getPurchaseId());

            context.storePurchaseOldQty(purchase.get().getPurchaseId(), oldQtyMap);

            /*
             * 2️⃣ Reverse OLD Stock
             */
            reverseOldStock(oldQtyMap);

            purchase.get().getPurchaseItems().clear();
            purchase.get().getPurchasePayments().clear();

            context.markInitialized(purchase.get());
        }

        PurchaseItem newPurchaseItem = new PurchaseItem();

        newPurchaseItem = purchaseItemEntityMapper.toEntity(
                purchase.get(),
                purchaseItemRow,
                item,
                newPurchaseItem
        );

        Purchase existingPurchase = purchase.get();
        existingPurchase.getPurchaseItems().add(newPurchaseItem);

//        // =====================================================
//        // ✅ STOCK MANAGEMENT
//        // =====================================================
//
//        if (item.getItemType().equals(ItemType.PRODUCT)) {
//
//            // 1️⃣ FIFO Batch
//            PurchaseBatch batch = new PurchaseBatch();
//            batch.setItem(item);
//            batch.setPurchaseId(existingPurchase.getPurchaseId());
//            batch.setQtyPurchased(newPurchaseItem.getQuantity());
//            batch.setQtyRemaining(newPurchaseItem.getQuantity());
//            batch.setPricePerQty(
//                    BigDecimal.valueOf(newPurchaseItem.getPricePerUnit()));
//            batch.setPurchaseDate(existingPurchase.getBillDate());
//            batch.setActive(true);
//
//            purchaseBatchRepository.save(batch);
//
//            // 2️⃣ Stock Transaction
//            StockTransaction tx = new StockTransaction();
//            tx.setItem(item);
//            tx.setType(StockTransactionType.PURCHASE);
//            tx.setQuantity(newPurchaseItem.getQuantity());
//            tx.setRate(
//                    BigDecimal.valueOf(newPurchaseItem.getPricePerUnit()));
//            tx.setAmount(
//                    BigDecimal.valueOf(newPurchaseItem.getQuantity())
//                            .multiply(BigDecimal.valueOf(newPurchaseItem.getPricePerUnit()))
//            );
//            tx.setTransactionDate(existingPurchase.getBillDate());
//            tx.setReference("PURCHASE-" + existingPurchase.getPurchaseId());
//
//            stockTransactionRepository.save(tx);
//
//            // 3️⃣ Snapshot Update
//            Double availableStock =
//                    stockTransactionRepository.totalStock(item.getItemId());
//
//            Double stockValue =
//                    availableStock == null || availableStock <= 0
//                            ? 0.0
//                            : purchaseBatchRepository.sumRemainingValue(item.getItemId());
//
//            item.setAvailableStock(
//                    availableStock != null ? availableStock : 0.0);
//
//            item.setStockValue(Math.max(stockValue, 0));
//
//            item.setPurchasePrice(newPurchaseItem.getPricePerUnit());
//
//            itemRepository.save(item);
//        }

        /*
         * ==========================================================
         * ✅ APPLY NEW STOCK
         * ==========================================================
         */
        if (item.getItemType().equals(ItemType.PRODUCT)) {

            addStock(purchase.get(), newPurchaseItem, item);
        }


        purchaseRepository.save(existingPurchase);
    }


    /*
     * ==========================================================
     * ✅ REVERSE OLD STOCK
     * ==========================================================
     */
    private void reverseOldStock(Map<Long, Double> oldQtyMap) {

        for (Map.Entry<Long, Double> entry : oldQtyMap.entrySet()) {

            Item item = itemRepository.findById(entry.getKey())
                    .orElseThrow();

            Double oldQty = entry.getValue();

            StockTransaction tx = new StockTransaction();
            tx.setItem(item);
            tx.setType(StockTransactionType.ADJUSTMENT_OUT);
            tx.setQuantity(oldQty);
            tx.setTransactionDate(LocalDate.now());
            tx.setReference("PURCHASE-REIMPORT-REVERSAL");

            stockTransactionRepository.save(tx);
        }
    }

    /*
     * ==========================================================
     * ✅ ADD NEW STOCK
     * ==========================================================
     */
    private void addStock(Purchase purchase, PurchaseItem purchaseItem, Item item) {

        /*
         * 1️⃣ FIFO Batch
         */
        PurchaseBatch batch = new PurchaseBatch();
        batch.setItem(item);
        batch.setPurchaseId(purchase.getPurchaseId());
        batch.setQtyPurchased(purchaseItem.getQuantity());
        batch.setQtyRemaining(purchaseItem.getQuantity());
        batch.setPricePerQty(BigDecimal.valueOf(purchaseItem.getPricePerUnit()));
        batch.setPurchaseDate(purchase.getBillDate());
        batch.setActive(true);

        purchaseBatchRepository.save(batch);

        /*
         * 2️⃣ Stock Transaction
         */
        StockTransaction tx = new StockTransaction();
        tx.setItem(item);
        tx.setType(StockTransactionType.PURCHASE);
        tx.setQuantity(purchaseItem.getQuantity());
        tx.setRate(BigDecimal.valueOf(purchaseItem.getPricePerUnit()));
        tx.setAmount(
                BigDecimal.valueOf(purchaseItem.getQuantity())
                        .multiply(BigDecimal.valueOf(purchaseItem.getPricePerUnit()))
        );
        tx.setTransactionDate(purchase.getBillDate());
        tx.setReference("PURCHASE-" + purchase.getPurchaseId());

        stockTransactionRepository.save(tx);

        /*
         * 3️⃣ Snapshot Update
         */
        Double availableStock =
                stockTransactionRepository.totalStock(item.getItemId());

        Double stockValue =
                availableStock == null || availableStock <= 0
                        ? 0.0
                        : purchaseBatchRepository.sumRemainingValue(item.getItemId());

        item.setAvailableStock(availableStock != null ? availableStock : 0.0);
        item.setStockValue(stockValue != null ? stockValue : 0.0);
        item.setPurchasePrice(purchaseItem.getPricePerUnit());

        itemRepository.save(item);
    }
}
