package com.example.techgicus_ebilling.techgicus_ebilling.dto.reportDto;

import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.PartyTransactionType;

import java.time.LocalDate;

public class PartyStatement {
    private Long id;
    private PartyTransactionType transactionType; // SALE, PURCHASE, PAYMENT, LEDGER, ACTIVITY
    private LocalDate transactionDate;
    private Long referenceId;
    private String referenceNumber;
   // private Double amount;
    private Double runningBalance;
    private Double debitAmount;
    private Double creditAmount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PartyTransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(PartyTransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }

    public Long getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(Long referenceId) {
        this.referenceId = referenceId;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

//    public Double getAmount() {
//        return amount;
//    }
//
//    public void setAmount(Double amount) {
//        this.amount = amount;
//    }

    public Double getRunningBalance() {
        return runningBalance;
    }

    public void setRunningBalance(Double runningBalance) {
        this.runningBalance = runningBalance;
    }

    public Double getDebitAmount() {
        return debitAmount;
    }

    public void setDebitAmount(Double debitAmount) {
        this.debitAmount = debitAmount;
    }

    public Double getCreditAmount() {
        return creditAmount;
    }

    public void setCreditAmount(Double creditAmount) {
        this.creditAmount = creditAmount;
    }
}
