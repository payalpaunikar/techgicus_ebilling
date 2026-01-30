package com.example.techgicus_ebilling.techgicus_ebilling.imports.handler;


import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.*;
import com.example.techgicus_ebilling.techgicus_ebilling.imports.context.ImportContext;
import com.example.techgicus_ebilling.techgicus_ebilling.imports.context.ImportError;
import com.example.techgicus_ebilling.techgicus_ebilling.imports.enumeration.ImportReportType;
import com.example.techgicus_ebilling.techgicus_ebilling.imports.sheet.TransactionSheetProcessor;
import com.example.techgicus_ebilling.techgicus_ebilling.imports.validator.PurchaseItemDetailsExcelValidator;
import com.example.techgicus_ebilling.techgicus_ebilling.imports.validator.PurchaseReportExcelValidator;
import com.example.techgicus_ebilling.techgicus_ebilling.repository.PurchaseRepository;
import com.example.techgicus_ebilling.techgicus_ebilling.repository.PurchaseReturnRepository;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PurchaseReportImportHandler implements ImportHandler{

    private final PurchaseReportExcelValidator purchaseReportExcelValidator;
    private final PurchaseItemDetailsExcelValidator purchaseItemDetailsExcelValidator;
    private final TransactionSheetProcessor transactionSheetProcessor;
    private final PurchaseRepository purchaseRepository;
    private final PurchaseReturnRepository purchaseReturnRepository;

    public PurchaseReportImportHandler(PurchaseReportExcelValidator purchaseReportExcelValidator, PurchaseItemDetailsExcelValidator purchaseItemDetailsExcelValidator, TransactionSheetProcessor transactionSheetProcessor, PurchaseRepository purchaseRepository, PurchaseReturnRepository purchaseReturnRepository) {
        this.purchaseReportExcelValidator = purchaseReportExcelValidator;
        this.purchaseItemDetailsExcelValidator = purchaseItemDetailsExcelValidator;
        this.transactionSheetProcessor = transactionSheetProcessor;
        this.purchaseRepository = purchaseRepository;
        this.purchaseReturnRepository = purchaseReturnRepository;
    }

    @Override
    public boolean supports(ImportReportType reportType) {
        return reportType == ImportReportType.PURCHASE;
    }

    @Override
    public String importReport(Workbook workbook, Company company) {

        Sheet purchaseSheet = workbook.getSheetAt(0);
        Sheet purchaseItemSheet = workbook.getSheetAt(1);

        purchaseReportExcelValidator.validateExcelFormat(purchaseSheet);
        purchaseItemDetailsExcelValidator.validateExcelFormat(purchaseItemSheet);

        ImportContext context = new ImportContext();

        int purchaseCount = transactionSheetProcessor.process(
                purchaseSheet,
                company,
                purchaseReportExcelValidator.getHeaderRowNumber(),
                purchaseReportExcelValidator.getTransactionTypeColumnIndex(),
                context
        );

        int itemCount = transactionSheetProcessor.process(
                purchaseItemSheet,
                company,
                purchaseItemDetailsExcelValidator.getHeaderRowNumber(),
                purchaseItemDetailsExcelValidator.getTransactionTypeColumnIndex(),
                context
        );

        finalizePurchases(context);

        return buildImportSummary(purchaseCount,itemCount,context);
    }


    private String buildImportSummary(
            int purchaseCount,
            int itemCount,
            ImportContext context
    ) {

        StringBuilder sb = new StringBuilder();

        sb.append("Import completed\n");
        sb.append("• Purchase processed: ").append(purchaseCount).append("\n");
        sb.append("• Purchase items processed: ").append(itemCount).append("\n");

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
    public void finalizePurchases(ImportContext context) {

        for (Purchase purchase : context.getPurchases()) {

            double totalAmount = 0.0;
            double totalTax = 0.0;

            for (PurchaseItem item : purchase.getPurchaseItems()) {
                totalAmount += item.getTotalAmount();
                totalTax += item.getTotalTaxAmount();
            }

            // double totalAmount = totalWithoutTax + totalTax;

            double totalWithoutTax = totalAmount - totalTax;

            purchase.setTotalAmountWithoutTax(totalWithoutTax);
            purchase.setTotalTaxAmount(totalTax);
            purchase.setTotalAmount(totalAmount);

            double received = purchase.getSendAmount() != null
                    ? purchase.getSendAmount()
                    : 0.0;

            purchase.setBalance(totalAmount - received);
            purchase.setIsPaid(purchase.getBalance() <= 0);
            purchase.setOverdue(!purchase.getIsPaid());

            if (purchase.getPurchasePayments().isEmpty()) {

                PurchasePayment payment = new PurchasePayment();
                payment.setPurchase(purchase);
                payment.setAmountPaid(purchase.getSendAmount());
                payment.setPaymentDate(purchase.getBillDate());
                payment.setPaymentDescription("Imported (auto-adjust)");

                purchase.getPurchasePayments().add(payment);

                //  salePaymentRepository.save(payment);
            }

            purchaseRepository.save(purchase);

        }

        for (PurchaseReturn purchaseReturn : context.getPurchaseReturns()) {

            double totalAmount = 0.0;
            double totalTax = 0.0;
            double totalQuantity =0.0;

            for (PurchaseReturnItem item : purchaseReturn.getPurchaseReturnItems()) {
                totalAmount += item.getTotalAmount();
                totalTax += item.getTotalTaxAmount();
                totalQuantity += item.getQuantity();
            }

            // double totalAmount = totalWithoutTax + totalTax;

            // saleReturn.setTotalAmountWithoutTax(totalWithoutTax);
            purchaseReturn.setTotalTaxAmount(totalTax);
            purchaseReturn.setTotalAmount(totalAmount);
            purchaseReturn.setTotalQuantity(totalQuantity);

            double received = purchaseReturn.getReceivedAmount() != null
                    ? purchaseReturn.getReceivedAmount()
                    : 0.0;

            purchaseReturn.setBalanceAmount(totalAmount - received);


            purchaseReturnRepository.save(purchaseReturn);

        }
    }
}
