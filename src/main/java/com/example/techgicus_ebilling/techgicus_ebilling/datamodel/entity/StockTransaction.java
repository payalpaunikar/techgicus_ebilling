package com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity;

import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.StockTransactionType;
import jakarta.persistence.*;

import java.time.LocalDate;



// this table is used for
//1)  Reports, 2) Invoice history,3) Item-wise transaction report,
//4) Audit logs, 5)Stock ledger, )GST / accounting reporting

//❌ Not used for stock calculation
//This table is only for history.

@Entity
public class StockTransaction {

     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     private Long id;

    private LocalDate transactionDate;
    private Double quantity;
    private Double totalAmount;

    @Enumerated(EnumType.STRING)
    private StockTransactionType transactionType; // PURCHASE, SALE, SALE_RETURN, PURCHASE_RETURN, OPENING_STOCK
    private String referenceNumber;


    private Double pricePerUnit;

    private Double openingStock;      // stock before transaction
    private Double closingStock;      // stock after transaction

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

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

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public StockTransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(StockTransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Double getPricePerUnit() {
        return pricePerUnit;
    }

    public void setPricePerUnit(Double pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }
}
