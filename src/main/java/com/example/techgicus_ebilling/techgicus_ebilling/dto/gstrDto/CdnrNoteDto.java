package com.example.techgicus_ebilling.techgicus_ebilling.dto.gstrDto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class CdnrNoteDto {
    private String gstinUinRecipient;
    private String receiverName;
    private String noteNumber;
    private LocalDate noteDate;
    private String noteType;            // e.g., "C" Credit, "D" Debit
    private String placeOfSupply;
    private String reverseCharge;
    private String noteSupplyType;
    private BigDecimal noteValue;
    private BigDecimal applicableTaxRate;
    private BigDecimal rate;
    private BigDecimal taxableValue;
    private BigDecimal cessAmount;

    public String getGstinUinRecipient() {
        return gstinUinRecipient;
    }

    public void setGstinUinRecipient(String gstinUinRecipient) {
        this.gstinUinRecipient = gstinUinRecipient;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getNoteNumber() {
        return noteNumber;
    }

    public void setNoteNumber(String noteNumber) {
        this.noteNumber = noteNumber;
    }

    public LocalDate getNoteDate() {
        return noteDate;
    }

    public void setNoteDate(LocalDate noteDate) {
        this.noteDate = noteDate;
    }

    public String getNoteType() {
        return noteType;
    }

    public void setNoteType(String noteType) {
        this.noteType = noteType;
    }

    public String getPlaceOfSupply() {
        return placeOfSupply;
    }

    public void setPlaceOfSupply(String placeOfSupply) {
        this.placeOfSupply = placeOfSupply;
    }

    public String getReverseCharge() {
        return reverseCharge;
    }

    public void setReverseCharge(String reverseCharge) {
        this.reverseCharge = reverseCharge;
    }

    public String getNoteSupplyType() {
        return noteSupplyType;
    }

    public void setNoteSupplyType(String noteSupplyType) {
        this.noteSupplyType = noteSupplyType;
    }

    public BigDecimal getNoteValue() {
        return noteValue;
    }

    public void setNoteValue(BigDecimal noteValue) {
        this.noteValue = noteValue;
    }

    public BigDecimal getApplicableTaxRate() {
        return applicableTaxRate;
    }

    public void setApplicableTaxRate(BigDecimal applicableTaxRate) {
        this.applicableTaxRate = applicableTaxRate;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public BigDecimal getTaxableValue() {
        return taxableValue;
    }

    public void setTaxableValue(BigDecimal taxableValue) {
        this.taxableValue = taxableValue;
    }

    public BigDecimal getCessAmount() {
        return cessAmount;
    }

    public void setCessAmount(BigDecimal cessAmount) {
        this.cessAmount = cessAmount;
    }
}
