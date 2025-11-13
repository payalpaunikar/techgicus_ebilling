package com.example.techgicus_ebilling.techgicus_ebilling.repository;


import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Company;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.SaleOrder;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.OrderType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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



    @Query(value = """
            SELECT  
           SUM(
            CASE 
                WHEN s.order_date BETWEEN 
                    CONCAT(YEAR(CURDATE()), '-01-01') 
                    AND CONCAT(YEAR(CURDATE()), '-12-31') 
                THEN s.total_quantity
                ELSE 0 
            END
        ) AS total_order 
        FROM sale_order s 
        WHERE s.company_id = :companyId
            """,nativeQuery = true)
    Double getTotalOrderOfCurrentYear(@Param("companyId")Long companyId);
}
