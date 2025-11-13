package com.example.techgicus_ebilling.techgicus_ebilling.repository;


import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Company;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.PurchaseReturn;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.reportDto.PurchaseReportDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PurchaseReturnRepository extends JpaRepository<PurchaseReturn,Long> {

    List<PurchaseReturn> findAllByCompanyAndReturnDateBetween(Company company, LocalDate startDate, LocalDate endDate);


    @Query("""
            SELECT new com.example.techgicus_ebilling.techgicus_ebilling.dto.reportDto.PurchaseReportDto
            (
            pu.purchaseReturnId AS purchaseId,
            pu.party.partyId AS partyId,
            pu.party.name AS name,
            pu.party.gstin AS gstin,
            pu.party.phoneNo AS phoneNo,
            pu.billNo AS billNumber,
            pu.billDate AS billDate,
            pu.paymentType AS paymentType,
            pu.description AS paymentDescription,
            pu.totalAmount AS totalAmount,
            pu.receivedAmount AS sendAmount,
            pu.balanceAmount AS balance
            ) 
            FROM PurchaseReturn pu
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
}
