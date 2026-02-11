package com.example.techgicus_ebilling.techgicus_ebilling.imports.processor;

import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.*;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.ItemType;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.StockTransactionType;
import com.example.techgicus_ebilling.techgicus_ebilling.imports.context.ImportContext;
import com.example.techgicus_ebilling.techgicus_ebilling.imports.dto.SaleItemRow;
import com.example.techgicus_ebilling.techgicus_ebilling.imports.extractor.SaleItemRowExtractor;
import com.example.techgicus_ebilling.techgicus_ebilling.imports.mapper.SaleItemEntityMapper;
import com.example.techgicus_ebilling.techgicus_ebilling.imports.mapper.SalePaymentEntityMapper;
import com.example.techgicus_ebilling.techgicus_ebilling.imports.service.SaleCalculationService;
import com.example.techgicus_ebilling.techgicus_ebilling.imports.utill.ModelUtill;
import com.example.techgicus_ebilling.techgicus_ebilling.repository.*;
import org.apache.poi.ss.usermodel.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class SaleItemTransactionProcessor implements TransactionProcessor{

    private final SaleItemRowExtractor saleItemRowExtractor;
    private final SaleRepository saleRepository;
    private final SaleItemRepository saleItemRepository;
    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;
    private final SaleItemEntityMapper saleItemEntityMapper;
    private final SaleCalculationService saleCalculationService;
    private final SalePaymentEntityMapper salePaymentEntityMapper;
    private final SalePaymentRepository salePaymentRepository;
    private final PurchaseBatchRepository purchaseBatchRepository;
    private final StockTransactionRepository stockTransactionRepository;


    private final static Logger log = LoggerFactory.getLogger(SaleItemTransactionProcessor.class);

    public SaleItemTransactionProcessor(SaleItemRowExtractor saleItemRowExtractor, SaleRepository saleRepository, SaleItemRepository saleItemRepository, ItemRepository itemRepository, CategoryRepository categoryRepository, SaleItemEntityMapper saleItemEntityMapper, SaleCalculationService saleCalculationService, SalePaymentEntityMapper salePaymentEntityMapper, SalePaymentRepository salePaymentRepository, PurchaseBatchRepository purchaseBatchRepository, StockTransactionRepository stockTransactionRepository) {
        this.saleItemRowExtractor = saleItemRowExtractor;
        this.saleRepository = saleRepository;
        this.saleItemRepository = saleItemRepository;
        this.itemRepository = itemRepository;
        this.categoryRepository = categoryRepository;
        this.saleItemEntityMapper = saleItemEntityMapper;
        this.saleCalculationService = saleCalculationService;
        this.salePaymentEntityMapper = salePaymentEntityMapper;
        this.salePaymentRepository = salePaymentRepository;
        this.purchaseBatchRepository = purchaseBatchRepository;
        this.stockTransactionRepository = stockTransactionRepository;
    }

    @Override
    public boolean supports(String transactionType) {
        log.info("transaction type is : "+transactionType);
        if (transactionType.toLowerCase().trim().equals("sale-item"))return true;
        return false;
    }

    @Override
    public void process(Row row, Company company, ImportContext context) {

        log.info("row number : "+row.getRowNum());

        SaleItemRow saleItemRow = saleItemRowExtractor.extract(row);

        Optional<Sale> sale = saleRepository.findByInvoiceNumberAndCompany(saleItemRow.getInvoiceNo(),company);

        if (sale.isEmpty()){
            if (saleItemRow.getInvoiceNo() == null || saleItemRow.getInvoiceNo().isEmpty()){
                context.addError(row.getSheet().getSheetName(),row.getRowNum()+1,"Invoice number is missing. Sale items require an existing Purchase Invoice.");
                return;
            }
            context.addError(row.getSheet().getSheetName(),row.getRowNum()+1,"Sale not found for invoice : "+saleItemRow.getInvoiceNo());
            return;
        }


       Item item = ModelUtill.findOrCreateItem(saleItemRow,
               company,
               itemRepository,
               categoryRepository);



        // 🔥 CLEAR ONLY ONCE PER SALE
        if (!context.isInitialized(sale.get())) {

            log.info("First time processing sale {}, clearing old items & payments",
                    sale.get().getInvoiceNumber());

            sale.get().getSaleItem().clear();
            sale.get().getSalePayments().clear();

            context.markInitialized(sale.get());
        }


        SaleItem newSaleItem = new SaleItem();

       newSaleItem = saleItemEntityMapper.toEntity(
               sale.get(),
               saleItemRow,
               item,
               newSaleItem
       );


      //  saleItemRepository.save(newSaleItem);

        Sale existingSale = sale.get();
        existingSale.getSaleItem().add(newSaleItem);

        // =====================================================
        // ✅ STOCK MANAGEMENT (THIS IS THE IMPORTANT PART)
        // =====================================================

        if (item.getItemType().equals(ItemType.PRODUCT)) {

            double qtyToSell = newSaleItem.getQuantity();

            List<PurchaseBatch> batches =
                    purchaseBatchRepository.findAvailableBatchesFIFO(item);

            for (PurchaseBatch batch : batches) {

                if (qtyToSell <= 0) break;

                double usedQty = Math.min(batch.getQtyRemaining(), qtyToSell);

                batch.setQtyRemaining(batch.getQtyRemaining() - usedQty);

                if (batch.getQtyRemaining() == 0) {
                    batch.setActive(false);
                }

                purchaseBatchRepository.save(batch);

                // ✅ STOCK TRANSACTION
                StockTransaction tx = new StockTransaction();
                tx.setItem(item);
                tx.setType(StockTransactionType.SALE);
                tx.setQuantity(-usedQty);
                tx.setRate(batch.getPricePerQty());

                tx.setAmount(
                        batch.getPricePerQty()
                                .multiply(BigDecimal.valueOf(usedQty))
                );

                tx.setTransactionDate(existingSale.getInvoceDate());
                tx.setReference("SALE-" + existingSale.getSaleId());

                stockTransactionRepository.save(tx);

                qtyToSell -= usedQty;
            }

            // ✅ NEGATIVE STOCK CASE
            if (qtyToSell > 0) {

                log.warn("Negative stock for item {}", item.getItemName());

                StockTransaction negativeTx = new StockTransaction();
                negativeTx.setItem(item);
                negativeTx.setType(StockTransactionType.SALE);
                negativeTx.setQuantity(-qtyToSell);
                negativeTx.setRate(BigDecimal.ZERO);
                negativeTx.setAmount(BigDecimal.ZERO);
                negativeTx.setTransactionDate(existingSale.getInvoceDate());
                negativeTx.setReference("SALE-" + existingSale.getSaleId());

                stockTransactionRepository.save(negativeTx);
            }

            // ✅ SNAPSHOT UPDATE
            Double availableStock =
                    stockTransactionRepository.totalStock(item.getItemId());

            Double stockValue =
                    availableStock == null || availableStock <= 0
                            ? 0.0
                            : purchaseBatchRepository.sumRemainingValue(item.getItemId());

            item.setAvailableStock(availableStock != null ? availableStock : 0.0);
            item.setStockValue(Math.max(stockValue, 0));

            itemRepository.save(item);
        }


     //  existingSale =  saleCalculationService.recalculateSaleTotals(existingSale);
        saleRepository.save(existingSale);


//        SalePayment salePayment = new SalePayment();
//        salePayment = salePaymentEntityMapper.toEntity(existingSale,salePayment);
//
//        salePaymentRepository.save(salePayment);
    }
}
