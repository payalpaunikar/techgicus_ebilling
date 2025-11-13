package com.example.techgicus_ebilling.techgicus_ebilling.dto.itemDto;

public class ItemSaleSummaryDto {
   private Long itemId;
   private String itemName;
   private Double totalSaleCount;

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Double getTotalSaleCount() {
        return totalSaleCount;
    }

    public void setTotalSaleCount(Double totalSaleCount) {
        this.totalSaleCount = totalSaleCount;
    }
}
