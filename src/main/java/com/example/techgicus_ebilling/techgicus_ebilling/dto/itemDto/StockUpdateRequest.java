package com.example.techgicus_ebilling.techgicus_ebilling.dto.itemDto;

import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.StockTransactionType;

import java.time.LocalDate;

public class StockUpdateRequest {

    private LocalDate transactionDate;

    private Double quantity;

    private Double pricePerUnit;

    private StockTransactionType operationType; // ADD or REDUCE


    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
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

    public StockTransactionType getOperationType() {
        return operationType;
    }

    public void setOperationType(StockTransactionType operationType) {
        this.operationType = operationType;
    }
}
