package com.example.techgicus_ebilling.techgicus_ebilling.service;


import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.*;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.DocumentType;
import com.example.techgicus_ebilling.techgicus_ebilling.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Year;

@Service
public class DocumentNumberService {

    private final SaleRepository saleRepository;
    private final SaleOrderRepository saleOrderRepository;
    private final SaleReturnRepository saleReturnRepository;
    private final PurchaseRepository purchaseRepository;
    private final PurchaseReturnRepository purchaseReturnRepository;
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final DeliveryChallanRepository deliveryChallanRepository;
    private final PaymentInRepository paymentInRepository;
    private final PaymentOutRepository paymentOutRepository;
    private final QuotationRepository quotationRepository;


    @Autowired
    public DocumentNumberService(SaleRepository saleRepository, SaleOrderRepository saleOrderRepository, SaleReturnRepository saleReturnRepository, PurchaseRepository purchaseRepository, PurchaseReturnRepository purchaseReturnRepository, PurchaseOrderRepository purchaseOrderRepository, DeliveryChallanRepository deliveryChallanRepository, PaymentInRepository paymentInRepository, PaymentOutRepository paymentOutRepository, QuotationRepository quotationRepository) {
        this.saleRepository = saleRepository;
        this.saleOrderRepository = saleOrderRepository;
        this.saleReturnRepository = saleReturnRepository;
        this.purchaseRepository = purchaseRepository;
        this.purchaseReturnRepository = purchaseReturnRepository;
        this.purchaseOrderRepository = purchaseOrderRepository;
        this.deliveryChallanRepository = deliveryChallanRepository;
        this.paymentInRepository = paymentInRepository;
        this.paymentOutRepository = paymentOutRepository;
        this.quotationRepository = quotationRepository;
    }

    public String generate(DocumentType type, Company company) {
        String year = String.valueOf(Year.now().getValue());
        long nextNumber = getNextNumber(type,company);
        return type.getPrefix() + "/" + year + "/" + nextNumber;
    }

    private long getNextNumber(DocumentType type,Company company) {
        // Decide which table to count based on type
        return switch (type) {
            case SALE -> {
                Sale latest =
                        saleRepository.findTopByCompanyOrderBySaleIdDesc(company);

                if (latest == null) {
                    yield 1L; // first invoice for this company
                }

                String invoice = latest.getInvoiceNumber(); // KE/2026/3
                long lastNumber = Long.parseLong(
                        invoice.substring(invoice.lastIndexOf("/") + 1)
                );

                yield lastNumber + 1;
            }
            case SALE_ORDER -> {
                SaleOrder latest =
                        saleOrderRepository.findTopByCompanyOrderBySaleOrderIdDesc(company);

                if (latest == null) {
                    yield 1L; // first invoice for this company
                }

                String invoice = latest.getOrderNo(); 
                long lastNumber = Long.parseLong(
                        invoice.substring(invoice.lastIndexOf("/") + 1)
                );

                yield lastNumber + 1;
            }
            
            case SALE_RETURN -> {
                SaleReturn latest =
                        saleReturnRepository.findTopByCompanyOrderBySaleReturnIdDesc(company);

                if (latest == null) {
                    yield 1L; // first invoice for this company
                }

                String invoice = latest.getReturnNo();
                long lastNumber = Long.parseLong(
                        invoice.substring(invoice.lastIndexOf("/") + 1)
                );

                yield lastNumber + 1;
            }
            case PURCHASE ->{
                Purchase latest =
                        purchaseRepository.findTopByCompanyOrderByPurchaseIdDesc(company);

                if (latest == null) {
                    yield 1L; // first invoice for this company
                }

                String invoice = latest.getBillNumber();
                long lastNumber = Long.parseLong(
                        invoice.substring(invoice.lastIndexOf("/") + 1)
                );

                yield lastNumber + 1;
            }
            case PURCHASE_ORDER -> {
                PurchaseOrder latest =
                        purchaseOrderRepository.findTopByCompanyOrderByPurchaseOrderIdDesc(company);

                if (latest == null) {
                    yield 1L; // first invoice for this company
                }

                String invoice = latest.getOrderNo();
                long lastNumber = Long.parseLong(
                        invoice.substring(invoice.lastIndexOf("/") + 1)
                );

                yield lastNumber + 1;
            }
            case PURCHASE_RETURN -> {
                PurchaseReturn latest =
                        purchaseReturnRepository.findTopByCompanyOrderByPurchaseReturnIdDesc(company);

                if (latest == null) {
                    yield 1L; // first invoice for this company
                }

                String invoice = latest.getReturnNo();
                long lastNumber = Long.parseLong(
                        invoice.substring(invoice.lastIndexOf("/") + 1)
                );

                yield lastNumber + 1;
            }
            case DELIVERY ->{
                DeliveryChallan latest =
                        deliveryChallanRepository.findTopByCompanyOrderByDeliveryChallanIdDesc(company);

                if (latest == null) {
                    yield 1L; // first invoice for this company
                }

                String invoice = latest.getChallanNo();
                long lastNumber = Long.parseLong(
                        invoice.substring(invoice.lastIndexOf("/") + 1)
                );

                yield lastNumber + 1;
            }
            case PAYMENT_IN -> {
                PaymentIn latest =
                        paymentInRepository.findTopByCompanyOrderByPaymentInIdDesc(company);

                if (latest == null) {
                    yield 1L; // first invoice for this company
                }

                String invoice = latest.getReceiptNo();
                long lastNumber = Long.parseLong(
                        invoice.substring(invoice.lastIndexOf("/") + 1)
                );

                yield lastNumber + 1;
            }
            case PAYMENT_OUT ->{
                PaymentOut latest = paymentOutRepository.findTopByCompanyOrderByPaymentOutIdDesc(company);

                if (latest == null) {
                    yield 1L; // first invoice for this company
                }

                String invoice = latest.getReceiptNo();
                long lastNumber = Long.parseLong(
                        invoice.substring(invoice.lastIndexOf("/") + 1)
                );

                yield lastNumber + 1;
            }
            case QUOTATION -> {
                Quotation latest =
                        quotationRepository.findTopByCompanyOrderByQuotationIdDesc(company);

                if (latest == null) {
                    yield 1L; // first invoice for this company
                }

                String invoice = latest.getReferenceNo();
                long lastNumber = Long.parseLong(
                        invoice.substring(invoice.lastIndexOf("/") + 1)
                );

                yield lastNumber + 1;
            }
        };
    }
}
