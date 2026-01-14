package com.example.techgicus_ebilling.techgicus_ebilling.imports.handler;

import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Company;
import com.example.techgicus_ebilling.techgicus_ebilling.imports.enumeration.ImportReportType;
import org.apache.poi.ss.usermodel.Workbook;

public interface ImportHandler {
    /**
     * Determines whether this handler supports the given report type.
     */
    boolean supports(ImportReportType reportType);

    /**
     * Imports the Excel workbook for this report.
     */
    String importReport(Workbook workbook, Company company);
}
