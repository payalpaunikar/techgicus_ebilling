package com.example.techgicus_ebilling.techgicus_ebilling.imports.validator;


import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class PurchaseReportExcelValidator extends BaseExcelValidatior{
    @Override
    protected String sheetNameKeyword() {
        return "PurchaseReport";
    }

    @Override
    protected Map<Integer, String> requiredHeaders() {
        Map<Integer, String> headers = new LinkedHashMap<>();

        headers.put(0, "Date");
        headers.put(1, "Order No");
        headers.put(2, "Invoice No");
        headers.put(3, "Party Name");
        headers.put(4, "Party Phone No.");
        headers.put(5, "Transaction Type");
        headers.put(6, "Total Amount");
        headers.put(7, "Payment Type");
        headers.put(8, "Received/Paid Amount");
        headers.put(9, "Balance Due");
        headers.put(10, "Due Date");
        headers.put(11, "Status");
        headers.put(12,"Description");

        return headers;
    }

    @Override
    protected int headerRowNumber() {
        return 2;
    }

    @Override
    protected int transactionTypeColumnIndex() {
        return 5;
    }
}
