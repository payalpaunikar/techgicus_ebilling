package com.example.techgicus_ebilling.techgicus_ebilling.dto.itemDto;

import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.StockAdjustmentType;

import java.time.LocalDate;

public class StockAdjustmentRequest {

    private Long itemId;

    // ADD or REDUCE
    private StockAdjustmentType type;

    private Double quantity;
    private Double pricePerUnit; // required only for ADD

    private LocalDate transactionDate;

  //  private String reason; // optional (Damage, Manual correction, etc.)

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public StockAdjustmentType getType() {
        return type;
    }

    public void setType(StockAdjustmentType type) {
        this.type = type;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public Double getPricePerUnit() {
        return pricePerUnit;
    }

    public void setPricePerUnit(Double pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }

    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }

//    public String getReason() {
//        return reason;
//    }
//
//    public void setReason(String reason) {
//        this.reason = reason;
//    }
}

