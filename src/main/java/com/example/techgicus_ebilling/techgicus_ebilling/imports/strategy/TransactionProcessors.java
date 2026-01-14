package com.example.techgicus_ebilling.techgicus_ebilling.imports.strategy;

import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Company;
import org.apache.poi.ss.usermodel.Row;

public interface TransactionProcessors {
    boolean supports(String transactionType);

    void process(Row row, Company company);
}
