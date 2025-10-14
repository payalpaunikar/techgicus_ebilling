package com.example.techgicus_ebilling.techgicus_ebilling.repository;


import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Company;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.SaleOrder;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.OrderType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SaleOrderRepository extends JpaRepository<SaleOrder,Long> {

    @Query("""
            SELECT s FROM SaleOrder s
            WHERE s.company.companyId = :companyId
            AND (:orderType IS NULL OR s.orderType = :orderType)
            """)
    List<SaleOrder> findAllByCompanyAndOptionalOrderType(Long companyId, OrderType orderType);
}
