package com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity;

import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.TaxRate;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.TaxType;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.Unit;
import jakarta.persistence.*;


@Entity
public class PurchaseReturnItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long purchaseReturnItemId;

//    private String name;

    private Double quantity;

    @Enumerated(EnumType.STRING)
    private Unit unit;

    private Double ratePerUnit;

    @Enumerated(EnumType.STRING)
    private TaxType taxType;

    private Double discountAmount;

    private Double totalTaxAmount;

    @Enumerated(EnumType.STRING)
    private TaxRate taxRate;

    private Double totalAmount;


    @ManyToOne
    @JoinColumn(name = "purchase_return_id")
    private PurchaseReturn purchaseReturn;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    private String itemDescription;

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Long getPurchaseReturnItemId() {
        return purchaseReturnItemId;
    }

    public void setPurchaseReturnItemId(Long purchaseReturnItemId) {
        this.purchaseReturnItemId = purchaseReturnItemId;
    }

//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public Double getRatePerUnit() {
        return ratePerUnit;
    }

    public void setRatePerUnit(Double ratePerUnit) {
        this.ratePerUnit = ratePerUnit;
    }

    public TaxType getTaxType() {
        return taxType;
    }

    public void setTaxType(TaxType taxType) {
        this.taxType = taxType;
    }

    public Double getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(Double discountAmount) {
        this.discountAmount = discountAmount;
    }

    public Double getTotalTaxAmount() {
        return totalTaxAmount;
    }

    public void setTotalTaxAmount(Double totalTaxAmount) {
        this.totalTaxAmount = totalTaxAmount;
    }

    public TaxRate getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(TaxRate taxRate) {
        this.taxRate = taxRate;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public PurchaseReturn getPurchaseReturn() {
        return purchaseReturn;
    }

    public void setPurchaseReturn(PurchaseReturn purchaseReturn) {
        this.purchaseReturn = purchaseReturn;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }
}
