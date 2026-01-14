package com.example.techgicus_ebilling.techgicus_ebilling.repository;


import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Company;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Party;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Sale;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.reportDto.SaleReportDto;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.revenueDto.MonthlyRevenue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface SaleRepository extends JpaRepository<Sale,Long> {

    List<Sale> findAllByCompanyOrderByInvoceDateDesc(Company company);

    Sale findByInvoiceNumberAndParty(String invoiceNo, Party party);

    Optional<Sale> findByInvoiceNumberAndCompany(String invoiceNo, Company company);

    long countByCompany(Company company);

    Sale findTopByCompanyOrderBySaleIdDesc(Company company);


    @Query("""
            SELECT s FROM Sale s WHERE
            s.company.companyId = :companyId
            AND (:partyId IS NULL OR s.party.partyId = :partyId)
            AND s.invoceDate BETWEEN :startDate AND :endDate
            ORDER BY s.invoceDate DESC
            """)
    List<Sale> findAllByFilter(@Param("companyId")Long companyId,
                               @Param("partyId")Long partyId,
                               @Param("startDate")LocalDate startDate,
                               @Param("endDate")LocalDate endDate);




    @Query(value = """
    SELECT 
        SUM(
            CASE 
                WHEN s.invoce_date BETWEEN 
                    CONCAT(YEAR(CURDATE()), '-01-01') 
                    AND CONCAT(YEAR(CURDATE()), '-12-31') 
                THEN s.total_amount 
                ELSE 0 
            END
        ) AS total_revenue
    FROM sale s WHERE s.company_id = :companyId
""", nativeQuery = true)
    Double findCurrentYearRevenue(@Param("companyId")Long companyId);


    @Query(value = """
    SELECT 
        SUM(
            CASE 
                WHEN s.invoce_date BETWEEN 
                   :startDate AND :endDate
                THEN s.total_amount 
                ELSE 0 
            END
        ) AS total_revenue
    FROM sale s WHERE s.company_id = :companyId
""", nativeQuery = true)
    Double findRevenueByDate(@Param("companyId")Long companyId,
                             @Param("startDate")LocalDate startDate,
                             @Param("endDate")LocalDate endDate);


//    @Query(value = """
//    SELECT
//        MONTH(s.invoce_date) AS monthNumber,
//        DATE_FORMAT(s.invoce_date, '%b') AS monthName,
//        SUM(s.total_amount) AS totalRevenue
//    FROM sale s
//    WHERE YEAR(s.invoce_date) = YEAR(CURDATE())
//    AND s.company_id = :companyId
//    GROUP BY MONTH(s.invoce_date), DATE_FORMAT(s.invoce_date, '%b')
//    ORDER BY MONTH(s.invoce_date)
//""", nativeQuery = true)
//    List<MonthlyRevenue> findMonthlyRevenueForCurrentYear(@Param("companyId")Long companyId);

    @Query(value = """
    WITH RECURSIVE months AS (
        SELECT 1 AS month_number
        UNION ALL
        SELECT month_number + 1 
        FROM months
        WHERE month_number < 12
    )
    SELECT 
        m.month_number AS monthNumber,
        DATE_FORMAT(STR_TO_DATE(CONCAT(YEAR(CURDATE()), '-', LPAD(m.month_number, 2, '0'), '-01'), '%Y-%m-%d'), '%b') AS monthName,
        COALESCE(SUM(s.total_amount), 0) AS totalRevenue
    FROM months m
    LEFT JOIN sale s 
        ON MONTH(s.invoce_date) = m.month_number
        AND YEAR(s.invoce_date) = YEAR(CURDATE())
        AND s.company_id = :companyId
    GROUP BY m.month_number
    ORDER BY m.month_number
""", nativeQuery = true)
    List<MonthlyRevenue> findMonthlyRevenueForCurrentYear(@Param("companyId") Long companyId);




    @Query("""
            SELECT new com.example.techgicus_ebilling.techgicus_ebilling.dto.reportDto.SaleReportDto
            (
            s.saleId AS saleId,
            s.party.partyId AS partyId,
            s.party.name AS name,
            s.party.gstin AS gstin,
            s.party.phoneNo AS phoneNo,
            s.invoiceNumber AS invoiceNumber,
            s.invoceDate AS invoceDate,
            s.paymentType AS paymentType,
            s.paymentDescription AS paymentDescription,
            s.totalAmount AS totalAmount,
            s.receivedAmount AS receivedAmount,
            s.balance AS balance
            ) 
            FROM Sale s
            WHERE s.company.companyId = :companyId
            AND (:partyId IS NULL OR s.party.partyId = :partyId)
            AND s.invoceDate BETWEEN :startDate AND :endDate
            ORDER BY s.invoceDate DESC
            """)
    List<SaleReportDto> findSalesReport(
            @Param("companyId")Long companyId,
            @Param("partyId")Long partyId,
            @Param("startDate")LocalDate startDate,
            @Param("endDate")LocalDate endDate
    );



    @Query("""
    SELECT COALESCE(SUM(s.totalTaxAmount), 0)
    FROM Sale s
    WHERE s.company.companyId = :companyId
    AND s.invoceDate BETWEEN :startDate AND :endDate
    """)
    Double sumTaxAmountByDate(@Param("companyId")Long companyId,
                              @Param("startDate")LocalDate startDate,
                              @Param("endDate")LocalDate endDate);




}
