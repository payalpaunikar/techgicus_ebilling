package com.example.techgicus_ebilling.techgicus_ebilling.repository;


import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Company;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SaleRepository extends JpaRepository<Sale,Long> {

    List<Sale> findAllByCompanyOrderByInvoceDateDesc(Company company);


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
}
