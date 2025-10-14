package com.example.techgicus_ebilling.techgicus_ebilling.dto.purchaseDto;

import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.PaymentType;

import java.time.LocalDate;

public class PurchasePaymentResponse {
    private Long paymentId;

    private String receiptNo;

    private LocalDate paymentDate;

    private Double amountPaid;

    private PaymentType paymentType;

    private String referenceNumber;

    private String paymentDescription;

    public PurchasePaymentResponse() {
    }

    public PurchasePaymentResponse(Long paymentId, String receiptNo, LocalDate paymentDate, Double amountPaid, PaymentType paymentType, String referenceNumber, String paymentDescription) {
        this.paymentId = paymentId;
        this.receiptNo = receiptNo;
        this.paymentDate = paymentDate;
        this.amountPaid = amountPaid;
        this.paymentType = paymentType;
        this.referenceNumber = referenceNumber;
        this.paymentDescription = paymentDescription;
    }

    public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
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
