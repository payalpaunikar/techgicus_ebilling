package com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity;

import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.TaxRate;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.TaxType;
import jakarta.persistence.*;

import java.time.LocalDateTime;


@Entity
public class PurchaseItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long purchaseItemId;

//    private String itemName;
//
//    private String itemHsnCode;

    private String itemDescription;

    private Double quantity;

//    @Enumerated(EnumType.STRING)
//    private Unit unit;

    private Double pricePerUnit;

    @Enumerated(EnumType.STRING)
    private TaxType pricePerUnitTaxType;

    @Enumerated(EnumType.STRING)
    private TaxRate taxRate;

    private Double totalTaxAmount;

    private Double totalAmount;

    @ManyToOne
    @JoinColumn(name = "purchase_id")
    private Purchase purchase;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    private LocalDateTime createdAt;
    private LocalDateTime updateAt;

    public PurchaseItem() {
    }


    public Long getPurchaseItemId() {
        return purchaseItemId;
    }

    public void setPurchaseItemId(Long purchaseItemId) {
        this.purchaseItemId = purchaseItemId;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
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

    public TaxType getPricePerUnitTaxType() {
        return pricePerUnitTaxType;
    }

    public void setPricePerUnitTaxType(TaxType pricePerUnitTaxType) {
        this.pricePerUnitTaxType = pricePerUnitTaxType;
    }

    public TaxRate getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(TaxRate taxRate) {
        this.taxRate = taxRate;
    }

    public Double getTotalTaxAmount() {
        return totalTaxAmount;
    }

    public void setTotalTaxAmount(Double totalTaxAmount) {
        this.totalTaxAmount = totalTaxAmount;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Purchase getPurchase() {
        return purchase;
    }

    public void setPurchase(Purchase purchase) {
        this.purchase = purchase;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(LocalDateTime updateAt) {
        this.updateAt = updateAt;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }
}
