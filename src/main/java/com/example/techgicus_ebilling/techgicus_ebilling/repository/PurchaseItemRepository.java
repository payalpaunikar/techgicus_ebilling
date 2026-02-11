package com.example.techgicus_ebilling.techgicus_ebilling.repository;


import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Purchase;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.PurchaseItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public interface PurchaseItemRepository extends JpaRepository<PurchaseItem,Long> {

    void deleteByPurchase(Purchase purchase);

    @Query("""
    SELECT pi.item.itemId, SUM(pi.quantity)
    FROM PurchaseItem pi
    WHERE pi.purchase.purchaseId = :purchaseId
    GROUP BY pi.item.itemId
""")
    Map<Long, Double> sumQuantityGroupByItem(@Param("purchaseId") Long purchaseId);


}
