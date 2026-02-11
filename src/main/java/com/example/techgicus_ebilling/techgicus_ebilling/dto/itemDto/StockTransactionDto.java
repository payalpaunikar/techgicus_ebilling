package com.example.techgicus_ebilling.techgicus_ebilling.dto.itemDto;

import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.StockTransactionType;

import java.time.LocalDate;

public class StockTransactionDto {
    private Long id;
    private LocalDate transactionDate;
    private StockTransactionType transactionType;
    private String reference;
    private Double quantity;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }

    public StockTransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(StockTransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }
}
