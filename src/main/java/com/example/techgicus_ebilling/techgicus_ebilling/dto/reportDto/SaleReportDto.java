package com.example.techgicus_ebilling.techgicus_ebilling.dto.reportDto;

import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.PaymentType;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.SaleTransactionType;

import java.time.LocalDate;

public class SaleReportDto {
    private Long saleId;
    private Long partyId;
    private String name;
    private String gstin;
    private String phoneNo;
    private String invoiceNumber;
    private LocalDate invoceDate;
    private SaleTransactionType saleTransactionType;
    private PaymentType paymentType;
    private String paymentDescription;
    private Double totalAmount;
    private Double receivedAmount;
    private Double balance;

    public SaleReportDto(Long saleId, Long partyId, String name, String gstin, String phoneNo, String invoiceNumber, LocalDate invoceDate, PaymentType paymentType, String paymentDescription, Double totalAmount, Double receivedAmount, Double balance) {
        this.saleId = saleId;
        this.partyId = partyId;
        this.name = name;
        this.gstin = gstin;
        this.phoneNo = phoneNo;
        this.invoiceNumber = invoiceNumber;
        this.invoceDate = invoceDate;
        this.paymentType = paymentType;
        this.paymentDescription = paymentDescription;
        this.totalAmount = totalAmount;
        this.receivedAmount = receivedAmount;
        this.balance = balance;
    }

    public Long getSaleId() {
        return saleId;
    }

    public void setSaleId(Long saleId) {
        this.saleId = saleId;
    }

    public Long getPartyId() {
        return partyId;
    }

    public void setPartyId(Long partyId) {
        this.partyId = partyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGstin() {
        return gstin;
    }

    public void setGstin(String gstin) {
        this.gstin = gstin;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public LocalDate getInvoceDate() {
        return invoceDate;
    }

    public void setInvoceDate(LocalDate invoceDate) {
        this.invoceDate = invoceDate;
    }



    public PaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public String getPaymentDescription() {
        return paymentDescription;
    }

    public void setPaymentDescription(String paymentDescription) {
        this.paymentDescription = paymentDescription;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Double getReceivedAmount() {
        return receivedAmount;
    }

    public void setReceivedAmount(Double receivedAmount) {
        this.receivedAmount = receivedAmount;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public SaleTransactionType getSaleTransactionType() {
        return saleTransactionType;
    }

    public void setSaleTransactionType(SaleTransactionType saleTransactionType) {
        this.saleTransactionType = saleTransactionType;
    }
}
