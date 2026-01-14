package com.example.techgicus_ebilling.techgicus_ebilling.dto.taxDto;

public class TaxComponentResponse {
    private Double rate;     // e.g. 9%
    private Double amount;

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
