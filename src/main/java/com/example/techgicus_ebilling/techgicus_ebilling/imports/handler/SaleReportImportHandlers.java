package com.example.techgicus_ebilling.techgicus_ebilling.imports.handler;

import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Company;
import com.example.techgicus_ebilling.techgicus_ebilling.imports.enumeration.ImportReportType;
import com.example.techgicus_ebilling.techgicus_ebilling.imports.sheet.TransactionSheetProcessor;
import com.example.techgicus_ebilling.techgicus_ebilling.imports.validator.SaleReportExcelValidator;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;

@Service
public class SaleReportImportHandlers implements ImportHandler {

    private final SaleReportExcelValidator validator;
    private final TransactionSheetProcessor transactionSheetProcessor;

    public SaleReportImportHandlers(SaleReportExcelValidator validator, TransactionSheetProcessor transactionSheetProcessor) {
        this.validator = validator;
        this.transactionSheetProcessor = transactionSheetProcessor;
    }

    @Override
    public boolean supports(ImportReportType reportType) {
        return reportType == ImportReportType.SALE;
    }

    @Override
    public String importReport(Workbook workbook, Company company) {

        Sheet saleSheet = workbook.getSheetAt(0);

        // validate the excel format first
        validator.validateExcelFormat(saleSheet);

        // process row
        int count = transactionSheetProcessor.process(
                saleSheet,
                company,
                validator.getHeaderRowNumber(),
                validator.getTransactionTypeColumnIndex()
        );

        return count + " sale transactions imported successfully";
    }
}
