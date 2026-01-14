package com.example.techgicus_ebilling.techgicus_ebilling.repository;


import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Company;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.DeliveryChallan;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.PurchaseOrder;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.ChallanType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeliveryChallanRepository extends JpaRepository<DeliveryChallan,Long> {

    long countByCompany(Company company);

    DeliveryChallan findTopByCompanyOrderByDeliveryChallanIdDesc(Company company);


    @Query("""
            SELECT d FROM DeliveryChallan d
            WHERE d.company = :company 
            AND (:challanType IS NULL OR d.challanType =:challanType)
            """)
    List<DeliveryChallan> findAllByCompanyAndOptionalChallanType(
            @Param("company") Company company,
            @Param("challanType") ChallanType challanType);
}
