package com.example.techgicus_ebilling.techgicus_ebilling.dto.gstrDto;

import java.math.BigDecimal;

public class AtDetailDto {
    private String placeOfSupply;
    private BigDecimal applicableTaxRate;
    private BigDecimal rate;
    private BigDecimal grossAdvanceReceived;
    private BigDecimal cessAmount;

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

    public BigDecimal getGrossAdvanceReceived() {
        return grossAdvanceReceived;
    }

    public void setGrossAdvanceReceived(BigDecimal grossAdvanceReceived) {
        this.grossAdvanceReceived = grossAdvanceReceived;
    }

    public BigDecimal getCessAmount() {
        return cessAmount;
    }

    public void setCessAmount(BigDecimal cessAmount) {
        this.cessAmount = cessAmount;
    }
}
