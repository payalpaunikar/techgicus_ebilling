package com.example.techgicus_ebilling.techgicus_ebilling.imports.excel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.example.techgicus_ebilling.techgicus_ebilling.imports.utill.ExcelUtil.getCellString;

public class ExcelValidatior {
    private Logger log = LoggerFactory.getLogger(ItemExcelValidator.class);


    public void validateExcelFormat(Sheet sheet) throws IllegalArgumentException {
        // 1. Check sheet name
        String sheetName = sheet.getSheetName().toLowerCase().trim();
        if (!sheetName.contains("items")) {
            throw new IllegalArgumentException(
                    "Wrong Excel format: Sheet name should contain 'Items'. Found: '" + sheet.getSheetName() + "'");
        }

        // 2. Check header row (row index 0 = row 1 in Excel)
        Row headerRow = sheet.getRow(0);
        if (headerRow == null) {
            throw new IllegalArgumentException("Header row is missing (expected at row 1).");
        }

        // 3. Define required columns and their positions
        // Adjust these indexes based on your actual Item Excel template
        Map<Integer, String> requiredHeaders = new LinkedHashMap<>();

        requiredHeaders.put(0, "Item Name");
        requiredHeaders.put(1, "Item Code");
        requiredHeaders.put(2, "Category");
        requiredHeaders.put(3, "HSN");
        requiredHeaders.put(4, "Sale Price");
        requiredHeaders.put(5, "Purchase price");
        requiredHeaders.put(6, "Discount Type");
        requiredHeaders.put(7, "Sale Discount");
        requiredHeaders.put(8, "Current stock quantity");
        requiredHeaders.put(9, "Minimum stock quantity");
        requiredHeaders.put(10, "Item Location");
        requiredHeaders.put(11, "Tax Rate");
        requiredHeaders.put(12, "Inclusive Of Tax");
        requiredHeaders.put(13, "Base Unit");
        requiredHeaders.put(14, "Secondary Unit");
        requiredHeaders.put(15, "Conversion Rate");


        // 4. Check each required header
        for (Map.Entry<Integer, String> entry : requiredHeaders.entrySet()) {
            int colIndex = entry.getKey();
            String expected = entry.getValue();

            Cell cell = headerRow.getCell(colIndex);
            String actual = getCellString(cell);

            if (actual == null || !actual.toLowerCase().contains(expected.toLowerCase())) {
                char columnLetter = (char) ('A' + colIndex);
                throw new IllegalArgumentException(
                        "Wrong format: Column " + columnLetter + " should contain '" + expected +
                                "', but found: '" + actual + "'");
            }
        }

        log.info("Item Excel format validated successfully.");
    }
}
