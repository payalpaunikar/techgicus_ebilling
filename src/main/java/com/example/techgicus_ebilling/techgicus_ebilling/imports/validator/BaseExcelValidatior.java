package com.example.techgicus_ebilling.techgicus_ebilling.imports.validator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static com.example.techgicus_ebilling.techgicus_ebilling.imports.utill.ExcelUtil.getCellString;

public abstract class BaseExcelValidatior {

    private Logger log = LoggerFactory.getLogger(ItemExcelValidator.class);

    // Each child must define these
    protected abstract String sheetNameKeyword();
    protected abstract Map<Integer, String> requiredHeaders();
    protected abstract int headerRowNumber();
    protected abstract int transactionTypeColumnIndex();

    public int getHeaderRowNumber(){
        return headerRowNumber();
    }

    public int getTransactionTypeColumnIndex() {
        return transactionTypeColumnIndex();
    }


    public void validateExcelFormat(Sheet sheet) throws IllegalArgumentException {
        // 1. Check sheet name
        // 1️⃣ Sheet name check
        String sheetName = sheet.getSheetName()
                .toLowerCase()
                .replaceAll("\\s+", "")
                .trim();
        if (!sheetName.contains(sheetNameKeyword().toLowerCase())) {
            throw new IllegalArgumentException(
                    "Wrong Excel format: Sheet name should contain '" +
                            sheetNameKeyword() + "'. Found: '" + sheet.getSheetName() + "'"
            );
        }

        // 2. Check header row (row index 0 = row 1 in Excel)
        Row headerRow = sheet.getRow(headerRowNumber());
        if (headerRow == null) {
            throw new IllegalArgumentException("Header row is missing (expected at row 1).");
        }

        // 3️⃣ Header validation
        for (Map.Entry<Integer, String> entry : requiredHeaders().entrySet()) {
            int colIndex = entry.getKey();
            String expected = entry.getValue();

            Cell cell = headerRow.getCell(colIndex);
            String actual = getCellString(cell);

            if (actual == null || !actual.toLowerCase().contains(expected.toLowerCase())) {
                char columnLetter = (char) ('A' + colIndex);
                throw new IllegalArgumentException(
                        "Wrong format: Column " + columnLetter +
                                " should contain '" + expected +
                                "', but found: '" + actual + "'"
                );
            }
        }


        log.info("{} Excel format validated successfully.", sheetNameKeyword());
    }

//    public int getColumnIndex(String headerKeyword) {
//
//        return requiredHeaders().entrySet().stream()
//                .filter(e -> e.getValue().equalsIgnoreCase(headerKeyword))
//                .map(Map.Entry::getKey)
//                .findFirst()
//                .orElseThrow(() ->
//                        new IllegalArgumentException(
//                                "Header '" + headerKeyword + "' not defined in requiredHeaders"
//                        )
//                );
//    }



}
