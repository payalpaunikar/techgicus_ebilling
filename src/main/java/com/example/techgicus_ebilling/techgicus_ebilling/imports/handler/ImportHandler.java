package com.example.techgicus_ebilling.techgicus_ebilling.imports.handler;

import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Company;
import com.example.techgicus_ebilling.techgicus_ebilling.imports.context.ImportContext;
import com.example.techgicus_ebilling.techgicus_ebilling.imports.context.ImportError;
import com.example.techgicus_ebilling.techgicus_ebilling.imports.dto.ImportSummaryResponse;
import com.example.techgicus_ebilling.techgicus_ebilling.imports.enumeration.ImportReportType;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.List;

public interface ImportHandler {
    /**
     * Determines whether this handler supports the given report type.
     */
    boolean supports(ImportReportType reportType);

    /**
     * Imports the Excel workbook for this report.
     */
    ImportSummaryResponse importReport(Workbook workbook, Company company);

    default ImportSummaryResponse buildImportSummary(
            String moduleName,
            int documentCount,
            int itemCount,
            ImportContext context
    ) {
        ImportSummaryResponse response = new ImportSummaryResponse();

        response.setModule(moduleName);
        response.setDocumentsProcessed(documentCount);
        response.setItemsProcessed(itemCount);
        response.setSuccess(!context.hasErrors());

        List<ImportError> errorList = context.getErrors()
                .stream()
                .map(error -> {
                    ImportError err = new ImportError(error.getSheetName(),error.getRowNumber(),error.getMessage());
//                    err.setSheetName(error.getSheetName());
//                    err.setRowNumber(error.getRowNumber());
//                    err.setMessage(error.getMessage());
                    return err;
                })
                .toList();

        response.setErrors(errorList);

        return response;
    }
}
