package com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity;

import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.PaymentType;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Entity
public class SalePayment {

      @Id
      @GeneratedValue(strategy = GenerationType.IDENTITY)
      private Long paymentId;

      private LocalDate paymentDate;

      private Double amountPaid;

      private PaymentType paymentType;

      private String referenceNumber;

      private String paymentDescription;

      private String receiptNo;


      @ManyToOne
      @JoinColumn(name = "sale_id")
      private Sale sale;

      private LocalDateTime createdAt;
      private LocalDateTime updateAt;

    public SalePayment() {
    }

    public SalePayment(Long paymentId, LocalDate paymentDate, Double amountPaid, PaymentType paymentType, String referenceNumber, String paymentDescription, String receiptNo, Sale sale, LocalDateTime createdAt, LocalDateTime updateAt) {
        this.paymentId = paymentId;
        this.paymentDate = paymentDate;
        this.amountPaid = amountPaid;
        this.paymentType = paymentType;
        this.referenceNumber = referenceNumber;
        this.paymentDescription = paymentDescription;
        this.receiptNo = receiptNo;
        this.sale = sale;
        this.createdAt = createdAt;
        this.updateAt = updateAt;
    }

    public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
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

    public Sale getSale() {
        return sale;
    }

    public void setSale(Sale sale) {
        this.sale = sale;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(LocalDateTime updateAt) {
        this.updateAt = updateAt;
    }

    public String getReceiptNo() {
        return receiptNo;
    }

    public void setReceiptNo(String receiptNo) {
        this.receiptNo = receiptNo;
    }
}
