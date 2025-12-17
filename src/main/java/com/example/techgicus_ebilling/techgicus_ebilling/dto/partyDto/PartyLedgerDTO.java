package com.example.techgicus_ebilling.techgicus_ebilling.dto.partyDto;

import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.PartyTransactionType;

import java.time.LocalDate;

public class PartyLedgerDTO {

    private Long ledgerId;
    private LocalDate date;
    private PartyTransactionType type;
    private Long referenceId;
    private String referenceNo; // invoice no , bill no, etc
    private Double debitAmount;
    private Double creditAmount;
    private Double runningBalance;
    private String description;


    public Long getLedgerId() {
        return ledgerId;
    }

    public void setLedgerId(Long ledgerId) {
        this.ledgerId = ledgerId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
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

    public Double getRunningBalance() {
        return runningBalance;
    }

    public void setRunningBalance(Double runningBalance) {
        this.runningBalance = runningBalance;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReferenceNo() {
        return referenceNo;
    }

    public void setReferenceNo(String referenceNo) {
        this.referenceNo = referenceNo;
    }
}
