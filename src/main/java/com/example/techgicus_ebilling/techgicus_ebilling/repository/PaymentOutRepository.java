package com.example.techgicus_ebilling.techgicus_ebilling.repository;


import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Company;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.PaymentIn;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.PaymentOut;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PaymentOutRepository extends JpaRepository<PaymentOut,Long> {

    long countByCompany(Company company);

    PaymentOut findTopByCompanyOrderByPaymentOutIdDesc(Company company);

    @Query("""
        SELECT p FROM PaymentOut p 
        WHERE p.company.companyId = :companyId
        AND (:partyId IS NULL OR p.party.partyId = :partyId)
        AND p.paymentDate BETWEEN :startDate AND :endDate
        Order by p.paymentDate DESC
    """)
    List<PaymentOut> findPaymentsByFilters(
            @Param("companyId") Long companyId,
            @Param("partyId") Long partyId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}
