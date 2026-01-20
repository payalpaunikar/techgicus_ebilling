package com.example.techgicus_ebilling.techgicus_ebilling.imports.processor;

import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Company;
import com.example.techgicus_ebilling.techgicus_ebilling.imports.context.ImportContext;
import org.apache.poi.ss.usermodel.Row;

public interface TransactionProcessor {
    boolean supports(String transactionType);
    void process(Row row, Company company, ImportContext context);
}

