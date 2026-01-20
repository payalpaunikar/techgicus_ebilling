package com.example.techgicus_ebilling.techgicus_ebilling.imports.handler;

import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Company;
import com.example.techgicus_ebilling.techgicus_ebilling.imports.context.ImportContext;
import com.example.techgicus_ebilling.techgicus_ebilling.imports.enumeration.ImportReportType;
import com.example.techgicus_ebilling.techgicus_ebilling.imports.sheet.TransactionSheetProcessor;
import com.example.techgicus_ebilling.techgicus_ebilling.imports.validator.SaleItemDetailsExcelValidator;
import com.example.techgicus_ebilling.techgicus_ebilling.imports.validator.SaleReportExcelValidator;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;

@Service
public class SaleReportImportHandlers implements ImportHandler {

    private final SaleReportExcelValidator saleReportExcelValidator;
    private final TransactionSheetProcessor transactionSheetProcessor;
    private final SaleItemDetailsExcelValidator saleItemDetailsExcelValidator;

    public SaleReportImportHandlers(SaleReportExcelValidator saleReportExcelValidator, TransactionSheetProcessor transactionSheetProcessor, SaleItemDetailsExcelValidator saleItemDetailsExcelValidator) {
        this.saleReportExcelValidator = saleReportExcelValidator;
        this.transactionSheetProcessor = transactionSheetProcessor;
        this.saleItemDetailsExcelValidator = saleItemDetailsExcelValidator;
    }

    @Override
    public boolean supports(ImportReportType reportType) {
        return reportType == ImportReportType.SALE;
    }

    @Override
    public String importReport(Workbook workbook, Company company) {

        Sheet saleSheet = workbook.getSheetAt(0);
        Sheet saleItemSheet = workbook.getSheetAt(1);

        // validate the excel format first
        saleReportExcelValidator.validateExcelFormat(saleSheet);
        saleItemDetailsExcelValidator.validateExcelFormat(saleItemSheet);

        ImportContext context = new ImportContext();

        // process row
        int count = transactionSheetProcessor.process(
                saleSheet,
                company,
                saleReportExcelValidator.getHeaderRowNumber(),
                saleReportExcelValidator.getTransactionTypeColumnIndex(),
                context
        );

        return count + " sale transactions imported successfully";
    }
}
