package com.example.techgicus_ebilling.techgicus_ebilling.dto.taxDto;

public class ItemTaxSummaryResponse {
   // private String itemName;
    private String hsnCode;

    private Double taxableAmount;

    private TaxComponentResponse cgst;
    private TaxComponentResponse sgst;

    private Double totalTaxAmount;


//    public String getItemName() {
//        return itemName;
//    }
//
//    public void setItemName(String itemName) {
//        this.itemName = itemName;
//    }

    public String getHsnCode() {
        return hsnCode;
    }

    public void setHsnCode(String hsnCode) {
        this.hsnCode = hsnCode;
    }

    public Double getTaxableAmount() {
        return taxableAmount;
    }

    public void setTaxableAmount(Double taxableAmount) {
        this.taxableAmount = taxableAmount;
    }

    public TaxComponentResponse getCgst() {
        return cgst;
    }

    public void setCgst(TaxComponentResponse cgst) {
        this.cgst = cgst;
    }

    public TaxComponentResponse getSgst() {
        return sgst;
    }

    public void setSgst(TaxComponentResponse sgst) {
        this.sgst = sgst;
    }

    public Double getTotalTaxAmount() {
        return totalTaxAmount;
    }

    public void setTotalTaxAmount(Double totalTaxAmount) {
        this.totalTaxAmount = totalTaxAmount;
    }


}
