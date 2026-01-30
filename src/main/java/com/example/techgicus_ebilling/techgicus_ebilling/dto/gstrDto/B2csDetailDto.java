package com.example.techgicus_ebilling.techgicus_ebilling.dto.gstrDto;

import java.math.BigDecimal;

public class B2csDetailDto {

    private String type;                // e.g., "OE"
    private String placeOfSupply;       // e.g., "27-Maharashtra"
    private BigDecimal applicableTaxRate;
    private BigDecimal rate;            // e.g., 18
    private BigDecimal taxableValue;    // e.g., 26429.66
    private BigDecimal cessAmount;      // e.g., 0
    private String eCommerceGstin;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
