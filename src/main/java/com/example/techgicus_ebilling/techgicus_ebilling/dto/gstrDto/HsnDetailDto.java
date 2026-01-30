package com.example.techgicus_ebilling.techgicus_ebilling.dto.gstrDto;

import java.math.BigDecimal;

public class HsnDetailDto {
    private int num;
    private String hsn;                 // e.g., "2710"
    private String description;         // e.g., "" (empty in sample)
    private String uqc;                 // e.g., "OTH-OTHERS"
    private BigDecimal totalQuantity;   // e.g., 9
    private BigDecimal totalValue;      // e.g., 27544
    private BigDecimal rate;            // e.g., 18
    private BigDecimal taxableValue;    // e.g., 23342.37
    private BigDecimal integratedTaxAmount;
    private BigDecimal centralTaxAmount;
    private BigDecimal stateUtTaxAmount;
    private BigDecimal cessAmount;

    public String getHsn() {
        return hsn;
    }

    public void setHsn(String hsn) {
        this.hsn = hsn;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUqc() {
        return uqc;
    }

    public void setUqc(String uqc) {
        this.uqc = uqc;
    }

    public BigDecimal getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(BigDecimal totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public BigDecimal getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(BigDecimal totalValue) {
        this.totalValue = totalValue;
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

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
