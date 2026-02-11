package com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class PurchaseBatch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Item item;

    private LocalDate purchaseDate;

    private double qtyPurchased;
    private double qtyRemaining;

    private BigDecimal pricePerQty;

    private Long purchaseId; // reference only

    private Boolean active = true;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public double getQtyPurchased() {
        return qtyPurchased;
    }

    public void setQtyPurchased(double qtyPurchased) {
        this.qtyPurchased = qtyPurchased;
    }

    public double getQtyRemaining() {
        return qtyRemaining;
    }

    public void setQtyRemaining(double qtyRemaining) {
        this.qtyRemaining = qtyRemaining;
    }

    public BigDecimal getPricePerQty() {
        return pricePerQty;
    }

    public void setPricePerQty(BigDecimal pricePerQty) {
        this.pricePerQty = pricePerQty;
    }

    public Long getPurchaseId() {
        return purchaseId;
    }

    public void setPurchaseId(Long purchaseId) {
        this.purchaseId = purchaseId;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
