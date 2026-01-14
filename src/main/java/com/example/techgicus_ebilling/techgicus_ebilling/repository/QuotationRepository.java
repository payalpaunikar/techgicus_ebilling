package com.example.techgicus_ebilling.techgicus_ebilling.repository;


import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Company;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.PaymentOut;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Quotation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuotationRepository extends JpaRepository<Quotation,Long> {

    long countByCompany(Company company);

    Quotation findTopByCompanyOrderByQuotationIdDesc(Company company);


    List<Quotation> findAllByCompany(Company company);
}
