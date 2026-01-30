package com.example.techgicus_ebilling.techgicus_ebilling.dto.gstrDto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class B2bInvoiceDto {
    private String gstinUinRecipient;
    private String receiverName;
    private String invoiceNumber;
    private LocalDate invoiceDate;
    private BigDecimal invoiceValue;
    private String placeOfSupply;
    private String reverseCharge;
    private BigDecimal applicableTaxRate;
    private String invoiceType;
    private String eCommerceGstin;
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

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
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

    public BigDecimal getApplicableTaxRate() {
        return applicableTaxRate;
    }

    public void setApplicableTaxRate(BigDecimal applicableTaxRate) {
        this.applicableTaxRate = applicableTaxRate;
    }

    public String getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(String invoiceType) {
        this.invoiceType = invoiceType;
    }

    public String geteCommerceGstin() {
        return eCommerceGstin;
    }

    public void seteCommerceGstin(String eCommerceGstin) {
        this.eCommerceGstin = eCommerceGstin;
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
