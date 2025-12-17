package com.example.techgicus_ebilling.techgicus_ebilling.dto.reportDto;

import java.util.List;

public class BillWiseProfitAndLossDetail {

   private List<BillWiseProfitAndLossItem> items;
   private Double saleAmount;
   private Double totalCost;
   private Double taxPayable;
   private Double tdsPayable;
   private Double total;

    public List<BillWiseProfitAndLossItem> getItems() {
        return items;
    }

    public void setItems(List<BillWiseProfitAndLossItem> items) {
        this.items = items;
    }

    public Double getSaleAmount() {
        return saleAmount;
    }

    public void setSaleAmount(Double saleAmount) {
        this.saleAmount = saleAmount;
    }

    public Double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(Double totalCost) {
        this.totalCost = totalCost;
    }

    public Double getTaxPayable() {
        return taxPayable;
    }

    public void setTaxPayable(Double taxPayable) {
        this.taxPayable = taxPayable;
    }

    public Double getTdsPayable() {
        return tdsPayable;
    }

    public void setTdsPayable(Double tdsPayable) {
        this.tdsPayable = tdsPayable;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }
}
