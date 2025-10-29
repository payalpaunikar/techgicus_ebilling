package com.example.techgicus_ebilling.techgicus_ebilling.repository;


import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Company;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.SaleReturn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SaleReturnRepository extends JpaRepository<SaleReturn,Long> {


    List<SaleReturn> findAllByCompanyAndInvoiceDateBetween(Company company, LocalDate startDate, LocalDate endDate);
}
