package com.example.techgicus_ebilling.techgicus_ebilling.repository;

import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Item;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.PurchaseBatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PurchaseBatchRepository extends JpaRepository<PurchaseBatch, Long> {

    List<PurchaseBatch> findByItemAndQtyRemainingGreaterThanOrderByPurchaseDateAsc(
            Item item, double qty);

    Optional<PurchaseBatch> findByItemAndPurchaseId(Item item, Long purchaseId);


    @Query("""
        SELECT pb
        FROM PurchaseBatch pb
        WHERE pb.item = :item
          AND pb.active = true
          AND pb.qtyRemaining > 0
        ORDER BY pb.purchaseDate ASC
    """)
    List<PurchaseBatch> findAvailableBatchesFIFO(@Param("item") Item item);

    @Query("""
   SELECT COALESCE(SUM(pb.qtyRemaining),0)
   FROM PurchaseBatch pb
   WHERE pb.item.id = :itemId AND pb.active = true
""")
    Double sumRemainingQty(Long itemId);

    @Query("""
   SELECT COALESCE(SUM(pb.qtyRemaining * pb.pricePerQty),0)
   FROM PurchaseBatch pb
   WHERE pb.item.id = :itemId AND pb.active = true
""")
    Double sumRemainingValue(Long itemId);

    @Query("""
        SELECT pb
        FROM PurchaseBatch pb
        WHERE pb.item = :item
          AND pb.qtyPurchased > pb.qtyRemaining
        ORDER BY pb.purchaseDate DESC
    """)
    List<PurchaseBatch> findConsumedBatchesLIFO(@Param("item") Item item);

    List<PurchaseBatch> findByPurchaseId(Long purchaseId);

    @Query("""
        SELECT pb
        FROM PurchaseBatch pb
        WHERE pb.item = :item
        ORDER BY pb.purchaseDate ASC
    """)
    List<PurchaseBatch> findAllByItemOrderByPurchaseDate(@Param("item") Item item);

    List<PurchaseBatch> findByItemOrderByPurchaseDateDesc(Item item);


    @Modifying
    @Query("""
    UPDATE PurchaseBatch b
    SET b.active = false,
        b.qtyRemaining = 0
    WHERE b.purchaseId = :purchaseId
""")
    void deactivateByPurchaseId(@Param("purchaseId") Long purchaseId);


    List<PurchaseBatch> findByItem(Item item);

}

