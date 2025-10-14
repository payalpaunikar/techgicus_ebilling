package com.example.techgicus_ebilling.techgicus_ebilling.repository;

import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.PurchaseOrder;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.SaleOrder;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.OrderType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder,Long> {

    @Query("""
            SELECT p FROM PurchaseOrder p
            WHERE p.company.companyId = :companyId
            AND (:orderType IS NULL OR p.orderType = :orderType)
            """)
    List<PurchaseOrder> findAllByCompanyAndOptionalOrderType(Long companyId, OrderType orderType);
}
