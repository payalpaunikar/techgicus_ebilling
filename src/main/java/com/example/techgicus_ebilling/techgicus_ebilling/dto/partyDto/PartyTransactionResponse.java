package com.example.techgicus_ebilling.techgicus_ebilling.dto.partyDto;

import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.PartyTransactionType;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class PartyTransactionResponse {

    private Long id;
    private PartyTransactionType transactionType; // SALE, PURCHASE, PAYMENT, LEDGER, ACTIVITY
    private LocalDate activityDate;


    private Long referenceId;

    private String referenceNumber;
    private Double amount;

    private Double runningBalance;

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

    public LocalDate getActivityDate() {
        return activityDate;
    }

    public void setActivityDate(LocalDate activityDate) {
        this.activityDate = activityDate;
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

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getRunningBalance() {
        return runningBalance;
    }

    public void setRunningBalance(Double runningBalance) {
        this.runningBalance = runningBalance;
    }
}
