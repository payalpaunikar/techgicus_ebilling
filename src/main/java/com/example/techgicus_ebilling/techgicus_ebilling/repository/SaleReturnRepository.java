package com.example.techgicus_ebilling.techgicus_ebilling.repository;


import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Company;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.SaleReturn;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.reportDto.SaleReportDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SaleReturnRepository extends JpaRepository<SaleReturn,Long> {


    List<SaleReturn> findAllByCompanyAndInvoiceDateBetween(Company company, LocalDate startDate, LocalDate endDate);


    @Query("""
            SELECT new com.example.techgicus_ebilling.techgicus_ebilling.dto.reportDto.SaleReportDto
            (
            s.saleReturnId AS saleId,
            s.party.partyId AS partyId,
            s.party.name AS name,
            s.party.gstin AS gstin,
            s.party.phoneNo AS phoneNo,
            s.invoiceNo AS invoiceNumber,
            s.invoiceDate AS invoceDate,
            s.paymentType AS paymentType,
            s.description AS paymentDescription,
            s.totalAmount AS totalAmount,
            s.paidAmount AS receivedAmount,
            s.balanceAmount AS balance
            ) 
            FROM SaleReturn s
            WHERE s.company.companyId = :companyId
            AND (:partyId IS NULL OR s.party.partyId = :partyId)
            AND s.invoiceDate BETWEEN :startDate AND :endDate
            ORDER BY s.invoiceDate DESC
            """)
    List<SaleReportDto> findSalesReport(
            @Param("companyId")Long companyId,
            @Param("partyId")Long partyId,
            @Param("startDate")LocalDate startDate,
            @Param("endDate")LocalDate endDate
    );



    @Query("""
    SELECT COALESCE(SUM(s.totalAmount), 0)
    FROM SaleReturn s
    WHERE s.company.companyId = :companyId
    AND s.returnDate BETWEEN :startDate AND :endDate
    """)
    Double sumCreditNoteByDate(@Param("companyId")Long companyId,
                               @Param("startDate")LocalDate startDate,
                               @Param("endDate")LocalDate endDate);


    @Query("""
    SELECT COALESCE(SUM(s.totalTaxAmount), 0)
    FROM SaleReturn s
    WHERE s.company.companyId = :companyId
    AND s.returnDate BETWEEN :startDate AND :endDate
    """)
    Double sumTaxAmountByDate(@Param("companyId")Long companyId,
                              @Param("startDate")LocalDate startDate,
                              @Param("endDate")LocalDate endDate);
}
