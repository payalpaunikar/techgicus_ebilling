package com.example.techgicus_ebilling.techgicus_ebilling.imports.context;

public class ImportError {

    private String sheetName;
    private int rowNumber;
    private String message;

    public ImportError(String sheetName, int rowNumber, String message) {
        this.sheetName = sheetName;
        this.rowNumber = rowNumber;
        this.message = message;
    }

    //    public ImportError(int rowNumber, String message) {
//        this.rowNumber = rowNumber;
//        this.message = message;
//    }

    public int getRowNumber() {
        return rowNumber;
    }

    public void setRowNumber(int rowNumber) {
        this.rowNumber = rowNumber;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }
}
