package com.example.techgicus_ebilling.techgicus_ebilling.dto.partyDto;

import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.PartyTransactionType;

import java.time.LocalDate;

public class PartyActivityDTO {

    private Long id;
    private LocalDate activityDate;
    private PartyTransactionType type;
    private Long referenceId;
    private String referenceNumber;
    private Double runningBalance;
    private Double amount;
    private String note;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getActivityDate() {
        return activityDate;
    }

    public void setActivityDate(LocalDate activityDate) {
        this.activityDate = activityDate;
    }

    public PartyTransactionType getType() {
        return type;
    }

    public void setType(PartyTransactionType type) {
        this.type = type;
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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
