package com.example.techgicus_ebilling.techgicus_ebilling.batch.dto;

import org.apache.poi.ss.usermodel.Row;


// This is just a small box to carry one Excel row + info about which file it came from
public class ExcelRowData {
    private Row row;           // The actual Excel row
    private String fileType;   // Like "part", "purchase", or "sale" – tells us which report this row belongs to

    public ExcelRowData() {
    }

    public ExcelRowData(Row row, String fileType) {
        this.row = row;
        this.fileType = fileType;
    }

    public Row getRow() {
        return row;
    }

    public void setRow(Row row) {
        this.row = row;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }
}
