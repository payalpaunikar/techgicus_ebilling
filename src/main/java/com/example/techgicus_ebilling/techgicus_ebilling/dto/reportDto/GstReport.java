package com.example.techgicus_ebilling.techgicus_ebilling.dto.reportDto;

public class GstReport {
    private String partyName;
    private Double saleTax;
    private Double purchaseAndExpenseTax;

    public String getPartyName() {
        return partyName;
    }

    public void setPartyName(String partyName) {
        this.partyName = partyName;
    }

    public Double getSaleTax() {
        return saleTax;
    }

    public void setSaleTax(Double saleTax) {
        this.saleTax = saleTax;
    }

    public Double getPurchaseAndExpenseTax() {
        return purchaseAndExpenseTax;
    }

    public void setPurchaseAndExpenseTax(Double purchaseAndExpenseTax) {
        this.purchaseAndExpenseTax = purchaseAndExpenseTax;
    }
}
