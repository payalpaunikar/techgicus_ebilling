package com.example.techgicus_ebilling.techgicus_ebilling.imports.handler;

import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.*;
import com.example.techgicus_ebilling.techgicus_ebilling.imports.context.ImportContext;
import com.example.techgicus_ebilling.techgicus_ebilling.imports.context.ImportError;
import com.example.techgicus_ebilling.techgicus_ebilling.imports.enumeration.ImportReportType;
import com.example.techgicus_ebilling.techgicus_ebilling.imports.sheet.TransactionSheetProcessor;
import com.example.techgicus_ebilling.techgicus_ebilling.imports.validator.SaleItemDetailsExcelValidator;
import com.example.techgicus_ebilling.techgicus_ebilling.imports.validator.SaleReportExcelValidator;
import com.example.techgicus_ebilling.techgicus_ebilling.repository.SalePaymentRepository;
import com.example.techgicus_ebilling.techgicus_ebilling.repository.SaleRepository;
import com.example.techgicus_ebilling.techgicus_ebilling.repository.SaleReturnRepository;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SaleReportImportHandler implements ImportHandler {

    private final SaleReportExcelValidator saleReportExcelValidator;
    private final TransactionSheetProcessor transactionSheetProcessor;
    private final SaleItemDetailsExcelValidator saleItemDetailsExcelValidator;
    private final SaleRepository saleRepository;
    private final SalePaymentRepository salePaymentRepository;
    private final SaleReturnRepository saleReturnRepository;

    public SaleReportImportHandler(SaleReportExcelValidator saleReportExcelValidator, TransactionSheetProcessor transactionSheetProcessor, SaleItemDetailsExcelValidator saleItemDetailsExcelValidator, SaleRepository saleRepository, SalePaymentRepository salePaymentRepository, SaleReturnRepository saleReturnRepository) {
        this.saleReportExcelValidator = saleReportExcelValidator;
        this.transactionSheetProcessor = transactionSheetProcessor;
        this.saleItemDetailsExcelValidator = saleItemDetailsExcelValidator;
        this.saleRepository = saleRepository;
        this.salePaymentRepository = salePaymentRepository;
        this.saleReturnRepository = saleReturnRepository;
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


        finalizeSales(context);

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


    @Transactional
    public void finalizeSales(ImportContext context) {

        for (Sale sale : context.getSales()) {

            double totalAmount = 0.0;
            double totalTax = 0.0;

            for (SaleItem item : sale.getSaleItem()) {
                totalAmount += item.getTotalAmount();
                totalTax += item.getTaxAmount();
            }

           // double totalAmount = totalWithoutTax + totalTax;

            double totalWithoutTax = totalAmount - totalTax;

            sale.setTotalAmountWithoutTax(totalWithoutTax);
            sale.setTotalTaxAmount(totalTax);
            sale.setTotalAmount(totalAmount);

            double received = sale.getReceivedAmount() != null
                    ? sale.getReceivedAmount()
                    : 0.0;

            sale.setBalance(totalAmount - received);
            sale.setPaid(sale.getBalance() <= 0);
            sale.setOverdue(!sale.getPaid());

            if (sale.getSalePayments().isEmpty()) {

                SalePayment payment = new SalePayment();
                payment.setSale(sale);
                payment.setAmountPaid(sale.getReceivedAmount());
                payment.setPaymentDate(sale.getInvoceDate());
                payment.setPaymentDescription("Imported (auto-adjust)");

                sale.getSalePayments().add(payment);

              //  salePaymentRepository.save(payment);
            }


            saleRepository.save(sale);

        }

        for (SaleReturn saleReturn : context.getSaleReturns()) {

            double totalAmount = 0.0;
            double totalTax = 0.0;
            double totalQuantity =0.0;

            for (SaleReturnItem item : saleReturn.getSaleReturnItems()) {
                totalAmount += item.getTotalAmount();
                totalTax += item.getTotalTaxAmount();
                totalQuantity += item.getQuantity();
            }

           // double totalAmount = totalWithoutTax + totalTax;

           // saleReturn.setTotalAmountWithoutTax(totalWithoutTax);
            saleReturn.setTotalTaxAmount(totalTax);
            saleReturn.setTotalAmount(totalAmount);
            saleReturn.setTotalQuantity(totalQuantity);

            double received = saleReturn.getPaidAmount() != null
                    ? saleReturn.getPaidAmount()
                    : 0.0;

            saleReturn.setBalanceAmount(totalAmount - received);


            saleReturnRepository.save(saleReturn);

        }
    }


}
