package com.example.techgicus_ebilling.techgicus_ebilling.dto.purchaseDto;

import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.PaymentType;

import java.time.LocalDate;

public class PurchasePaymentRequest {
    private String receiptNo;

    private LocalDate paymentDate;

    private Double amountPaid;

    private PaymentType paymentType;

    private String referenceNumber;

    private String paymentDescription;

    public PurchasePaymentRequest() {
    }

    public PurchasePaymentRequest(String receiptNo, LocalDate paymentDate, Double amountPaid, PaymentType paymentType, String referenceNumber, String paymentDescription) {
        this.receiptNo = receiptNo;
        this.paymentDate = paymentDate;
        this.amountPaid = amountPaid;
        this.paymentType = paymentType;
        this.referenceNumber = referenceNumber;
        this.paymentDescription = paymentDescription;
    }

    public String getReceiptNo() {
        return receiptNo;
    }

    public void setReceiptNo(String receiptNo) {
        this.receiptNo = receiptNo;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public Double getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(Double amountPaid) {
        this.amountPaid = amountPaid;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public String getPaymentDescription() {
        return paymentDescription;
    }

    public void setPaymentDescription(String paymentDescription) {
        this.paymentDescription = paymentDescription;
    }
}
