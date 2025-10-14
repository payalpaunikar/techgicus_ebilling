package com.example.techgicus_ebilling.techgicus_ebilling.repository;


import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Company;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Purchase;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase,Long> {

    List<Purchase> findAllByCompanyOrderByBillDateDesc(Company company);

    @Query("""
            SELECT p FROM Purchase p WHERE
            p.company.companyId = :companyId
            AND (:partyId IS NULL OR p.party.partyId = :partyId)
            AND p.billDate BETWEEN :startDate AND :endDate
            ORDER BY p.billDate DESC
            """)
    List<Purchase> findAllByFilter(@Param("companyId")Long companyId,
                               @Param("partyId")Long partyId,
                               @Param("startDate") LocalDate startDate,
                               @Param("endDate")LocalDate endDate);
}
