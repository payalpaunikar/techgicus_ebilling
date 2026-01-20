package com.example.techgicus_ebilling.techgicus_ebilling.imports.handler;

import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Company;
import com.example.techgicus_ebilling.techgicus_ebilling.imports.context.ImportContext;
import com.example.techgicus_ebilling.techgicus_ebilling.imports.context.ImportError;
import com.example.techgicus_ebilling.techgicus_ebilling.imports.enumeration.ImportReportType;
import com.example.techgicus_ebilling.techgicus_ebilling.imports.sheet.TransactionSheetProcessor;
import com.example.techgicus_ebilling.techgicus_ebilling.imports.validator.SaleItemDetailsExcelValidator;
import com.example.techgicus_ebilling.techgicus_ebilling.imports.validator.SaleReportExcelValidator;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;

@Service
public class SaleReportImportHandler implements ImportHandler {

    private final SaleReportExcelValidator saleReportExcelValidator;
    private final TransactionSheetProcessor transactionSheetProcessor;
    private final SaleItemDetailsExcelValidator saleItemDetailsExcelValidator;

    public SaleReportImportHandler(SaleReportExcelValidator saleReportExcelValidator, TransactionSheetProcessor transactionSheetProcessor, SaleItemDetailsExcelValidator saleItemDetailsExcelValidator) {
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
        int saleCount = transactionSheetProcessor.process(
                saleSheet,
                company,
                saleReportExcelValidator.getHeaderRowNumber(),
                saleReportExcelValidator.getTransactionTypeColumnIndex(),
                context
        );

        int itemCount = transactionSheetProcessor.process(
                saleItemSheet,
                company,
                saleItemDetailsExcelValidator.getHeaderRowNumber(),
                saleItemDetailsExcelValidator.getTransactionTypeColumnIndex(),
                context
        );

        return buildImportSummary(saleCount, itemCount, context);
    }

    private String buildImportSummary(
            int saleCount,
            int itemCount,
            ImportContext context
    ) {

        StringBuilder sb = new StringBuilder();

        sb.append("Import completed\n");
        sb.append("• Sales processed: ").append(saleCount).append("\n");
        sb.append("• Sale items processed: ").append(itemCount).append("\n");

        if (context.hasErrors()) {
            sb.append("\n⚠ Errors (")
                    .append(context.getErrors().size())
                    .append("):\n");

            for (ImportError error : context.getErrors()) {
                sb.append("Row ")
                        .append(error.getRowNumber())
                        .append(": ")
                        .append(error.getMessage())
                        .append("\n");
            }
        } else {
            sb.append("\n✔ No errors found");
        }

        return sb.toString();
    }

}
