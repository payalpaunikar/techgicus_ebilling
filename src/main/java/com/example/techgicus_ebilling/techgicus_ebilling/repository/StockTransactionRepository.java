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

//    Optional<StockTransaction> findByReferenceAndItemAndTransactionType(
//            String refernceNo, Item item, StockTransactionType stockTransactionType);


    @Query("""
    SELECT COALESCE(SUM(st.amount), 0)
    FROM StockTransaction st
    WHERE st.item.company.companyId = :companyId
    AND st.transactionDate BETWEEN :startDate AND :endDate
    """)
    Double sumClosingStock(@Param("companyId")Long companyId,
                           @Param("startDate") LocalDate startDate,
                           @Param("endDate")LocalDate endDate);

    List<StockTransaction> findByItem(Item item);

//    @Query("""
//        SELECT st FROM StockTransaction st
//        WHERE st.item = :item
//        AND st.transactionType IN :types
//        ORDER BY st.transactionDate ASC, st.id ASC
//    """)
//    List<StockTransaction> findStockInTransactionsFIFO(
//            @Param("item") Item item,
//            @Param("types") Set<StockTransactionType> types
//    );

//    @Query("""
//    SELECT
//        COALESCE(SUM(st.qtyIn),0) - COALESCE(SUM(st.qtyOut),0)
//    FROM StockTransaction st
//    WHERE st.item = :item
//""")
//    Double calculateAvailableStock(@Param("item") Item item);


//    @Query("""
//        SELECT COALESCE(SUM(st.qtyIn), 0)
//        FROM StockTransaction st
//        WHERE st.item = :item
//    """)
//    Double sumQtyInByItem(@Param("item") Item item);

//    @Query("""
//        SELECT COALESCE(SUM(st.qtyOut), 0)
//        FROM StockTransaction st
//        WHERE st.item = :item
//    """)
//    Double sumQtyOutByItem(@Param("item") Item item);


//    @Query("""
//    SELECT st
//    FROM StockTransaction st
//    WHERE st.item = :item
//      AND st.transactionType IN ('OPENING_STOCK','PURCHASE')
//    ORDER BY st.transactionDate ASC
//""")
//    List<StockTransaction> findStockInsFIFO(@Param("item") Item item);


    Optional<StockTransaction> findByItemAndType(
            Item item,
            StockTransactionType transactionType
    );

    void deleteByTypeAndReference(
            StockTransactionType type,
            String referenceNumber
    );

//    @Query("select coalesce(max(s.balanceQty),0) from StockTransaction s where s.item = :item")
//    double findLastBalance(@Param("item") Item item);

   // List<StockTransaction> findByItemOrderByTxDateAsc(Item item);

    List<StockTransaction> findByReference(String reference);

    Optional<StockTransaction> findByItemAndReference(Item item,String reference);

    @Query("""
        select coalesce(sum(t.quantity),0)
        from StockTransaction t
        where t.item.id = :itemId
    """)
    Double totalStock(@Param("itemId") Long itemId);


    List<StockTransaction> findByReferenceOrderByIdDesc(String reference);

     void deleteByReference(String reference);
}
