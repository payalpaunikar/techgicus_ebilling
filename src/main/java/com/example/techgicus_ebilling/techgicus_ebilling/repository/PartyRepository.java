package com.example.techgicus_ebilling.techgicus_ebilling.repository;

import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Company;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Party;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.reportDto.PartyReportProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface PartyRepository extends JpaRepository<Party,Long> {
    List<Party> findByCompany(Company company);



     @Query("""
              SELECT
                     p.partyId AS partyId,
                     p.name AS partyName,
                     p.gstin AS gstin,
                     p.phoneNo AS phoneNo,
                     p.gstType AS gstType,
                     p.state AS state,
                     p.emailId AS emailId,
                     p.billingAddress AS billingAddress,
                     p.shipingAddress AS shipingAddress,
                     COALESCE(SUM(l.debitAmount), 0) AS totalDebit,
                     COALESCE(SUM(l.creditAmount), 0) AS totalCredit
                 FROM Party p
                 LEFT JOIN PartyLedger l ON l.party.partyId = p.partyId
                 WHERE p.company.companyId = :companyId
                 GROUP BY  p.partyId,
                     p.name,
                     p.gstin,
                     p.phoneNo,
                     p.gstType,
                     p.state,
                     p.emailId,
                     p.billingAddress,
                     p.shipingAddress
             """)
     List<PartyReportProjection> getAllPartyReport(@Param("companyId")Long companyId);


}
