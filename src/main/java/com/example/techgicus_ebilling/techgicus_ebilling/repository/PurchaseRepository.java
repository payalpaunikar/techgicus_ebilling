package com.example.techgicus_ebilling.techgicus_ebilling.repository;


import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Company;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Purchase;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Sale;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.SaleReturn;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.reportDto.PurchaseReportDto;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.reportDto.SaleReportDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase,Long> {

    long countByCompany(Company company);

    Optional<Purchase> findByBillNumberAndCompany(String billNo,Company company);

    Purchase findTopByCompanyOrderByPurchaseIdDesc(Company company);


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

    @Query("""
            SELECT new com.example.techgicus_ebilling.techgicus_ebilling.dto.reportDto.PurchaseReportDto
            (
            pu.purchaseId AS purchaseId,
            pu.party.partyId AS partyId,
            pu.party.name AS name,
            pu.party.gstin AS gstin,
            pu.party.phoneNo AS phoneNo,
            pu.billNumber AS billNumber,
            pu.billDate AS billDate,
            pu.paymentType AS paymentType,
            pu.paymentDescription AS paymentDescription,
            pu.totalAmount AS totalAmount,
            pu.sendAmount AS sendAmount,
            pu.balance AS balance
            ) 
            FROM Purchase pu
            WHERE pu.company.companyId = :companyId
            AND (:partyId IS NULL OR pu.party.partyId = :partyId)
            AND pu.billDate BETWEEN :startDate AND :endDate
            ORDER BY pu.billDate DESC
            """)
    List<PurchaseReportDto> findPurchaseReport(
            @Param("companyId")Long companyId,
            @Param("partyId")Long partyId,
            @Param("startDate")LocalDate startDate,
            @Param("endDate")LocalDate endDate
    );


    @Query("""
    SELECT COALESCE(SUM(p.totalAmount), 0)
    FROM Purchase p
    WHERE p.company.companyId = :companyId
    AND p.billDate BETWEEN :startDate AND :endDate
    """)
    Double sumPurchaseByDate(@Param("companyId")Long companyId,
                             @Param("startDate")LocalDate startDate,
                             @Param("endDate")LocalDate endDate);


    @Query("""
    SELECT COALESCE(SUM(p.totalTaxAmount), 0)
    FROM Purchase p
    WHERE p.company.companyId = :companyId
    AND p.billDate BETWEEN :startDate AND :endDate
    """)
    Double sumTaxAmountByDate(@Param("companyId")Long companyId,
                              @Param("startDate")LocalDate startDate,
                              @Param("endDate")LocalDate endDate);

    @Query("""
        SELECT DISTINCT pu
        FROM Purchase pu
        LEFT JOIN FETCH pu.party p
        LEFT JOIN FETCH pu.purchaseItems pi
        LEFT JOIN FETCH pi.item i
        WHERE pu.company.id = :companyId
          AND pu.billDate BETWEEN :startDate AND :endDate
        ORDER BY pu.billDate, pu.billNumber
    """)
    List<Purchase> findForGstr2(
            @Param("companyId") Long companyId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}
