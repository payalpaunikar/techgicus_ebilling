package com.example.techgicus_ebilling.techgicus_ebilling.repository;


import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Item;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.StockTransaction;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.StockTransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface StockTransactionRepository extends JpaRepository<StockTransaction,Long> {

    Optional<StockTransaction> findByReferenceNumberAndItemAndTransactionType(
            String refernceNo, Item item, StockTransactionType stockTransactionType);


    @Query("""
    SELECT COALESCE(SUM(st.totalAmount), 0)
    FROM StockTransaction st
    WHERE st.item.company.companyId = :companyId
    AND st.transactionDate BETWEEN :startDate AND :endDate
    """)
    Double sumClosingStock(@Param("companyId")Long companyId,
                           @Param("startDate") LocalDate startDate,
                           @Param("endDate")LocalDate endDate);

    List<StockTransaction> findByItem(Item item);

}
