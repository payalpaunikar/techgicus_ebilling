package com.example.techgicus_ebilling.techgicus_ebilling.imports.validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;


@Component
public class ItemExcelValidator extends BaseExcelValidatior {

    private Logger log = LoggerFactory.getLogger(ItemExcelValidator.class);


    @Override
    protected String sheetNameKeyword() {
        return "items";
    }

    @Override
    protected Map<Integer, String> requiredHeaders() {
        Map<Integer, String> headers = new LinkedHashMap<>();

        headers.put(0, "Item Name");
        headers.put(1, "Item Code");
        headers.put(2, "Category");
        headers.put(3, "HSN");
        headers.put(4, "Sale Price");
        headers.put(5, "Purchase price");
        headers.put(6, "Discount Type");
        headers.put(7, "Sale Discount");
        headers.put(8, "Current stock quantity");
        headers.put(9, "Minimum stock quantity");
        headers.put(10, "Item Location");
        headers.put(11, "Tax Rate");
        headers.put(12, "Inclusive Of Tax");
        headers.put(13, "Base Unit");
        headers.put(14, "Secondary Unit");
        headers.put(15, "Conversion Rate");

        return headers;
    }

    @Override
    protected int headerRowNumber() {
        return 0;
    }

    @Override
    protected int transactionTypeColumnIndex() {
        return 0;
    }
}
