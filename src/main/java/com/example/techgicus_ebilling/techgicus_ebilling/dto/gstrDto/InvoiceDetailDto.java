package com.example.techgicus_ebilling.techgicus_ebilling.dto.gstrDto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class InvoiceDetailDto {
    private String gstinUin;            // Customer GSTIN/UIN (empty if B2C)
    private String partyName;           // e.g., "B K Motors"
    private String transactionType;     // e.g., "Sale"
    private String invoiceNo;           // e.g., "315"
    private LocalDate invoiceDate;      // e.g., 01/03/2023
    private BigDecimal invoiceValue;    // e.g., 7720
    private BigDecimal rate;            // e.g., 18
    private BigDecimal cessRate;        // e.g., 0
    private BigDecimal taxableValue;    // e.g., 6542.37
    private String reverseCharge;       // "N" or "Y"
    private BigDecimal integratedTaxAmount;  // e.g., 0
    private BigDecimal centralTaxAmount;     // e.g., 588.81
    private BigDecimal stateUtTaxAmount;     // e.g., 588.81
    private BigDecimal cessAmount;           // e.g., 0
    private String placeOfSupply;// e.g., "Maharashtra"

    public String getGstinUin() {
        return gstinUin;
    }

    public void setGstinUin(String gstinUin) {
        this.gstinUin = gstinUin;
    }

    public String getPartyName() {
        return partyName;
    }

    public void setPartyName(String partyName) {
        this.partyName = partyName;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public LocalDate getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(LocalDate invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public BigDecimal getInvoiceValue() {
        return invoiceValue;
    }

    public void setInvoiceValue(BigDecimal invoiceValue) {
        this.invoiceValue = invoiceValue;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public BigDecimal getCessRate() {
        return cessRate;
    }

    public void setCessRate(BigDecimal cessRate) {
        this.cessRate = cessRate;
    }

    public BigDecimal getTaxableValue() {
        return taxableValue;
    }

    public void setTaxableValue(BigDecimal taxableValue) {
        this.taxableValue = taxableValue;
    }

    public String getReverseCharge() {
        return reverseCharge;
    }

    public void setReverseCharge(String reverseCharge) {
        this.reverseCharge = reverseCharge;
    }

    public BigDecimal getIntegratedTaxAmount() {
        return integratedTaxAmount;
    }

    public void setIntegratedTaxAmount(BigDecimal integratedTaxAmount) {
        this.integratedTaxAmount = integratedTaxAmount;
    }

    public BigDecimal getCentralTaxAmount() {
        return centralTaxAmount;
    }

    public void setCentralTaxAmount(BigDecimal centralTaxAmount) {
        this.centralTaxAmount = centralTaxAmount;
    }

    public BigDecimal getStateUtTaxAmount() {
        return stateUtTaxAmount;
    }

    public void setStateUtTaxAmount(BigDecimal stateUtTaxAmount) {
        this.stateUtTaxAmount = stateUtTaxAmount;
    }

    public BigDecimal getCessAmount() {
        return cessAmount;
    }

    public void setCessAmount(BigDecimal cessAmount) {
        this.cessAmount = cessAmount;
    }

    public String getPlaceOfSupply() {
        return placeOfSupply;
    }

    public void setPlaceOfSupply(String placeOfSupply) {
        this.placeOfSupply = placeOfSupply;
    }
}
