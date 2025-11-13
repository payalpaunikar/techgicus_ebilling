package com.example.techgicus_ebilling.techgicus_ebilling.dto.reportDto;

import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.PaymentType;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.PurchaseTransactionType;

import java.time.LocalDate;

public class PurchaseReportDto {
    private Long purchaseId;
    private Long partyId;
    private String name;
    private String gstin;
    private String phoneNo;
    private String billNumber;
    private LocalDate billDate;
    private PurchaseTransactionType purchaseTransactionType;
    private PaymentType paymentType;
    private String paymentDescription;
    private Double totalAmount;
    private Double sendAmount;
    private Double balance;

    public PurchaseReportDto(Long purchaseId, Long partyId, String name, String gstin, String phoneNo, String billNumber, LocalDate billDate, PaymentType paymentType, String paymentDescription, Double totalAmount, Double sendAmount, Double balance) {
        this.purchaseId = purchaseId;
        this.partyId = partyId;
        this.name = name;
        this.gstin = gstin;
        this.phoneNo = phoneNo;
        this.billNumber = billNumber;
        this.billDate = billDate;
        this.paymentType = paymentType;
        this.paymentDescription = paymentDescription;
        this.totalAmount = totalAmount;
        this.sendAmount = sendAmount;
        this.balance = balance;
    }

    public Long getPurchaseId() {
        return purchaseId;
    }

    public void setPurchaseId(Long purchaseId) {
        this.purchaseId = purchaseId;
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

    public String getBillNumber() {
        return billNumber;
    }

    public void setBillNumber(String billNumber) {
        this.billNumber = billNumber;
    }

    public LocalDate getBillDate() {
        return billDate;
    }

    public void setBillDate(LocalDate billDate) {
        this.billDate = billDate;
    }

    public PurchaseTransactionType getPurchaseTransactionType() {
        return purchaseTransactionType;
    }

    public void setPurchaseTransactionType(PurchaseTransactionType purchaseTransactionType) {
        this.purchaseTransactionType = purchaseTransactionType;
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

    public Double getSendAmount() {
        return sendAmount;
    }

    public void setSendAmount(Double sendAmount) {
        this.sendAmount = sendAmount;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }
}
