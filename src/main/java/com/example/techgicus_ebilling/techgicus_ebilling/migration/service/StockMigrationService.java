package com.example.techgicus_ebilling.techgicus_ebilling.migration.service;


import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.*;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.ItemType;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.StockTransactionType;
import com.example.techgicus_ebilling.techgicus_ebilling.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class StockMigrationService {

    private final PurchaseRepository purchaseRepository;
    private final SaleRepository saleRepository;
    private final PurchaseBatchRepository purchaseBatchRepository;
    private final StockTransactionRepository stockTransactionRepository;
    private final ItemRepository itemRepository;
    private final StockEngineService stockEngineService;

    public StockMigrationService(PurchaseRepository purchaseRepository, SaleRepository saleRepository, PurchaseBatchRepository purchaseBatchRepository, StockTransactionRepository stockTransactionRepository, ItemRepository itemRepository, StockEngineService stockEngineService) {
        this.purchaseRepository = purchaseRepository;
        this.saleRepository = saleRepository;
        this.purchaseBatchRepository = purchaseBatchRepository;
        this.stockTransactionRepository = stockTransactionRepository;
        this.itemRepository = itemRepository;
        this.stockEngineService = stockEngineService;
    }

    private final Logger log = LoggerFactory.getLogger(StockMigrationService.class);

    @Transactional
    public void rebuildStockFromScratch() {

        log.info("🚀 STOCK MIGRATION STARTED");

        /*
         * ============================================================
         * 1️⃣ CLEAN OLD STOCK DATA
         * ============================================================
         */
        purchaseBatchRepository.deleteAll();
        stockTransactionRepository.deleteAll();

        log.info("✅ Old stock cleared");

        /*
         * ============================================================
         * 2️⃣ REBUILD OPENING STOCK 🔥🔥🔥 (CRITICAL FIX)
         * ============================================================
         */
        List<Item> items = itemRepository.findAll();

        log.info("Replaying opening stock → {}", items.size());

        for (Item item : items) {

            if (!item.getItemType().equals(ItemType.PRODUCT))
                continue;

            Double openingQty = item.getStockOpeningQty();
            Double openingRate = item.getStockPricePerQty();
            LocalDate openingDate = item.getStockOpeningDate();

            if (openingQty != null && openingQty > 0) {

                log.info("Rebuilding opening stock for item → {}", item.getItemName());

                stockEngineService.addOpeningStock(
                        item,
                        openingQty,
                        openingRate,
                        openingDate != null ? openingDate : LocalDate.now()
                );
            }
        }

        /*
         * ============================================================
         * 3️⃣ REPLAY PURCHASES (STOCK IN)
         * ============================================================
         */
        List<Purchase> purchases = purchaseRepository.findAll();

        log.info("Replaying purchases → {}", purchases.size());

        for (Purchase purchase : purchases) {

            for (PurchaseItem pi : purchase.getPurchaseItems()) {

                Item item = pi.getItem();

                if (!item.getItemType().equals(ItemType.PRODUCT))
                    continue;

                stockEngineService.addPurchaseStock(
                        item,
                        purchase.getPurchaseId(),
                        pi.getQuantity(),
                        pi.getPricePerUnit(),
                        purchase.getBillDate()
                );
            }
        }

        /*
         * ============================================================
         * 4️⃣ REPLAY SALES (FIFO CONSUMPTION)
         * ============================================================
         */
        List<Sale> sales = saleRepository.findAll();

        log.info("Replaying sales → {}", sales.size());

        for (Sale sale : sales) {

            for (SaleItem si : sale.getSaleItem()) {

                Item item = si.getItem();

                if (!item.getItemType().equals(ItemType.PRODUCT))
                    continue;

                stockEngineService.consumeStockFIFO(
                        item,
                        sale.getSaleId(),
                        si.getQuantity(),
                        sale.getInvoceDate()
                );
            }
        }

        /*
         * ============================================================
         * 5️⃣ FINAL SNAPSHOT RECALCULATION
         * ============================================================
         */
        log.info("Rebuilding item snapshots → {}", items.size());

        for (Item item : items) {

            if (!item.getItemType().equals(ItemType.PRODUCT)) {
                item.setAvailableStock(0.0);
                item.setStockValue(0.0);
                itemRepository.save(item);
                continue;
            }

            stockEngineService.recalcItemSnapshot(item);
        }

        log.info("✅ STOCK MIGRATION COMPLETED SUCCESSFULLY");
    }

}
