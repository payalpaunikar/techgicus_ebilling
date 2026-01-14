package com.example.techgicus_ebilling.techgicus_ebilling.imports.utill;


import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.PaymentType;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public final class ExcelUtil {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private ExcelUtil() {} // Prevent instantiation

    public static String getCellString(Cell cell) {
        if (cell == null) return "";

        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue().trim();
            case NUMERIC -> {
                double value = cell.getNumericCellValue();
                yield (value % 1 == 0)
                        ? String.valueOf((long) value)
                        : String.valueOf(value);
            }
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            case FORMULA -> cell.getCellFormula();
            default -> "";
        };
    }

    public static Double getCellDouble(Cell cell) {
        if (cell == null) return 0.0;

        if (cell.getCellType() == CellType.NUMERIC) {
            return cell.getNumericCellValue();
        }

        try {
            return Double.parseDouble(getCellString(cell));
        } catch (Exception e) {
            return 0.0;
        }
    }

    public static boolean isRowEmpty(Row row) {
        if (row == null) return true;

        for (Cell cell : row) {
            if (cell.getCellType() != CellType.BLANK) {
                return false;
            }
        }
        return true;
    }

    public static LocalDate getCellLocalDate(Cell cell){
        String cellStr = cell.getStringCellValue().trim();
        LocalDate date = cellStr.isEmpty() ? null : LocalDate.parse(cellStr, DATE_FORMATTER);
        return date;
    }

    public static PaymentType parsePaymentType(Cell cell) {
        try {
            return PaymentType.valueOf(getCellString(cell).trim().toUpperCase());
        } catch (Exception e) {
            return PaymentType.CASH; // default or handle as needed
        }
    }
}
