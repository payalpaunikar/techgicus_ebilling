package com.example.techgicus_ebilling.techgicus_ebilling.imports.strategy.purchase.processor;

import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Company;
import com.example.techgicus_ebilling.techgicus_ebilling.imports.strategy.TransactionProcessor;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Service;

@Service
public class PurchaseTransactionProcessor implements TransactionProcessor {
    @Override
    public boolean supports(String transactionType) {
        return false;
    }

    @Override
    public void process(Row row, Company company) {

    }
}
