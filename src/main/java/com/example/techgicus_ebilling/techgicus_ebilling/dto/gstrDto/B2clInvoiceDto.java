package com.example.techgicus_ebilling.techgicus_ebilling.dto.gstrDto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class B2clInvoiceDto {
    private String invoiceNumber;
    private LocalDate invoiceDate;
    private BigDecimal invoiceValue;
    private String placeOfSupply;
    private BigDecimal applicableTaxRate;
    private BigDecimal rate;
    private BigDecimal taxableValue;
    private BigDecimal cessAmount;
    private String eCommerceGstin;

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

    public String geteCommerceGstin() {
        return eCommerceGstin;
    }

    public void seteCommerceGstin(String eCommerceGstin) {
        this.eCommerceGstin = eCommerceGstin;
    }
}
