package com.example.techgicus_ebilling.techgicus_ebilling.imports.validator;

import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class SaleItemDetailsExcelValidator extends BaseExcelValidatior {
    @Override
    protected String sheetNameKeyword() {
        return "ItemDetails";
    }

    @Override
    protected Map<Integer, String> requiredHeaders() {
        Map<Integer, String> headers = new LinkedHashMap<>();

        headers.put(0, "Date");
        headers.put(1, "Invoice No./Txn No.");
        headers.put(2, "Party Name");
        headers.put(3, "Item Name");
        headers.put(4, "Item Code");
        headers.put(5, "HSN/SAC");
        headers.put(6, "Category");
        headers.put(7, "Quantity");
        headers.put(8, "Unit");
        headers.put(9, "UnitPrice");
        headers.put(10, "Discount Percent");
        headers.put(11, "Discount");
        headers.put(12,"Tax Percent");
        headers.put(13, "Tax");
        headers.put(14,"Transaction Type");
        headers.put(15,"Amount");

        return headers;
    }

    @Override
    protected int headerRowNumber() {
        return 2;
    }

    @Override
    protected int transactionTypeColumnIndex() {
        return 14;
    }
}
