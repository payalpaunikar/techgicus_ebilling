package com.example.techgicus_ebilling.techgicus_ebilling.service;


import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Company;
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
            case SALE -> saleRepository.countByCompany(company)+1;
            case SALE_ORDER -> saleOrderRepository.countByCompany(company) + 1;
            case SALE_RETURN -> saleReturnRepository.countByCompany(company)+1;
            case PURCHASE -> purchaseRepository.countByCompany(company) + 1;
            case PURCHASE_ORDER -> purchaseOrderRepository.countByCompany(company)+1;
            case PURCHASE_RETURN -> purchaseReturnRepository.countByCompany(company)+1;
            case DELIVERY -> deliveryChallanRepository.countByCompany(company)+1;
            case PAYMENT_IN -> paymentInRepository.countByCompany(company)+1;
            case PAYMENT_OUT -> paymentOutRepository.countByCompany(company)+1;
            case QUOTATION -> quotationRepository.countByCompany(company) + 1;
        };
    }
}
