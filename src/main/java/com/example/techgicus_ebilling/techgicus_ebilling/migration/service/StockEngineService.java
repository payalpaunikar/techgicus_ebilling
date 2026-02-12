package com.example.techgicus_ebilling.techgicus_ebilling.migration.service;

import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Item;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.PurchaseBatch;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.StockTransaction;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.StockTransactionType;
import com.example.techgicus_ebilling.techgicus_ebilling.repository.ItemRepository;
import com.example.techgicus_ebilling.techgicus_ebilling.repository.PurchaseBatchRepository;
import com.example.techgicus_ebilling.techgicus_ebilling.repository.StockTransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class StockEngineService {

    private final PurchaseBatchRepository purchaseBatchRepository;
    private final StockTransactionRepository stockTransactionRepository;
    private final ItemRepository itemRepository;

    public StockEngineService(PurchaseBatchRepository purchaseBatchRepository, StockTransactionRepository stockTransactionRepository, ItemRepository itemRepository) {
        this.purchaseBatchRepository = purchaseBatchRepository;
        this.stockTransactionRepository = stockTransactionRepository;
        this.itemRepository = itemRepository;
    }

    /*
     * ============================================================
     * ✅ OPENING STOCK
     * ============================================================
     */
    @Transactional
    public void addOpeningStock(Item item,
                                Double qty,
                                Double rate,
                                LocalDate date) {

        if (qty == null || qty <= 0) return;

        // 1️⃣ FIFO Batch
        PurchaseBatch batch = new PurchaseBatch();
        batch.setItem(item);
        batch.setPurchaseId(0L);
        batch.setQtyPurchased(qty);
        batch.setQtyRemaining(qty);
        batch.setPricePerQty(BigDecimal.valueOf(rate != null ? rate : 0));
        batch.setPurchaseDate(date);
        batch.setActive(true);

        purchaseBatchRepository.save(batch);

        // 2️⃣ Stock Transaction
        StockTransaction tx = new StockTransaction();
        tx.setItem(item);
        tx.setType(StockTransactionType.OPENING);
        tx.setQuantity(qty);
        tx.setRate(BigDecimal.valueOf(rate != null ? rate : 0));
        tx.setAmount(BigDecimal.valueOf(qty)
                .multiply(BigDecimal.valueOf(rate != null ? rate : 0)));
        tx.setTransactionDate(date);
        tx.setReference("OPENING");

        stockTransactionRepository.save(tx);

        recalcItemSnapshot(item);
    }

    /*
     * ============================================================
     * ✅ PURCHASE STOCK
     * ============================================================
     */
    @Transactional
    public void addPurchaseStock(Item item,
                                 Long purchaseId,
                                 Double qty,
                                 Double rate,
                                 LocalDate date) {

        if (qty == null || qty <= 0) return;

        // 1️⃣ FIFO Batch
        PurchaseBatch batch = new PurchaseBatch();
        batch.setItem(item);
        batch.setPurchaseId(purchaseId);
        batch.setQtyPurchased(qty);
        batch.setQtyRemaining(qty);
        batch.setPricePerQty(BigDecimal.valueOf(rate));
        batch.setPurchaseDate(date);
        batch.setActive(true);

        purchaseBatchRepository.save(batch);

        // 2️⃣ Transaction
        StockTransaction tx = new StockTransaction();
        tx.setItem(item);
        tx.setType(StockTransactionType.PURCHASE);
        tx.setQuantity(qty);
        tx.setRate(BigDecimal.valueOf(rate));
        tx.setAmount(BigDecimal.valueOf(qty)
                .multiply(BigDecimal.valueOf(rate)));
        tx.setTransactionDate(date);
        tx.setReference("PURCHASE-" + purchaseId);

        stockTransactionRepository.save(tx);

        recalcItemSnapshot(item);
    }

    /*
     * ============================================================
     * ✅ SALES FIFO CONSUMPTION
     * ============================================================
     */
    @Transactional
    public void consumeStockFIFO(Item item,
                                 Long saleId,
                                 Double qty,
                                 LocalDate date) {

        if (qty == null || qty <= 0) return;

        Double remaining = qty;

        List<PurchaseBatch> batches =
                purchaseBatchRepository.findAvailableBatchesFIFO(item);

        for (PurchaseBatch batch : batches) {

            if (remaining <= 0) break;

            double available = batch.getQtyRemaining();
            if (available <= 0) continue;

            double consume = Math.min(available, remaining);

            batch.setQtyRemaining(available - consume);
            batch.setActive(batch.getQtyRemaining() > 0);

            purchaseBatchRepository.save(batch);

            remaining -= consume;
        }

        if (remaining > 0) {
            throw new RuntimeException("Stock not available for item "
                    + item.getItemName());
        }

        StockTransaction tx = new StockTransaction();
        tx.setItem(item);
        tx.setType(StockTransactionType.SALE);
        tx.setQuantity(-qty);
        tx.setTransactionDate(date);
        tx.setReference("SALE-" + saleId);

        stockTransactionRepository.save(tx);

        recalcItemSnapshot(item);
    }

    /*
     * ============================================================
     * ✅ STOCK DELTA (RE-IMPORT SAFE 🔥)
     * ============================================================
     */
    @Transactional
    public void applyStockDelta(Item item,
                                Double deltaQty,
                                Double rate,
                                LocalDate date,
                                String reference) {

        if (deltaQty == 0) return;

        if (deltaQty > 0) {
            addPurchaseStock(item, -1L, deltaQty, rate, date);
        } else {
            reduceStock(item, Math.abs(deltaQty), date, reference);
        }
    }

    /*
     * ============================================================
     * ✅ SAFE STOCK REDUCTION
     * ============================================================
     */
    private void reduceStock(Item item,
                             Double qty,
                             LocalDate date,
                             String reference) {

        Double available =
                stockTransactionRepository.totalStock(item.getItemId());

        if (available + (-qty) < 0) {
            throw new RuntimeException("Cannot reduce stock. Already consumed.");
        }

        StockTransaction tx = new StockTransaction();
        tx.setItem(item);
        tx.setType(StockTransactionType.ADJUSTMENT_OUT);
        tx.setQuantity(-qty);
        tx.setTransactionDate(date);
        tx.setReference(reference);

        stockTransactionRepository.save(tx);

        recalcItemSnapshot(item);
    }

    /*
     * ============================================================
     * ✅ SNAPSHOT RECALCULATION (FINAL TRUTH)
     * ============================================================
     */
    @Transactional
    public void recalcItemSnapshot(Item item) {

        Double stock =
                stockTransactionRepository.totalStock(item.getItemId());

        Double stockValue =
                stock == null || stock <= 0
                        ? 0.0
                        : purchaseBatchRepository.sumRemainingValue(item.getItemId());

        item.setAvailableStock(stock != null ? stock : 0.0);
        item.setStockValue(stockValue != null ? stockValue : 0.0);

        itemRepository.save(item);
    }
}
