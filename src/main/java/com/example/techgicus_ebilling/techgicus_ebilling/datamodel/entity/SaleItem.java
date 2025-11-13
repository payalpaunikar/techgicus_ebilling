package com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity;

import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.TaxRate;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.TaxType;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.Unit;
import jakarta.persistence.*;
import software.amazon.awssdk.services.s3.endpoints.internal.Value;

import java.time.LocalDateTime;


@Entity
public class SaleItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long saleItemId;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    private String itemName;

    private String itemHsnCode;

    private String itemDescription;

    private Integer quantity;

    @Enumerated(EnumType.STRING)
    private Unit unit;

    private Double pricePerUnit;

    @Enumerated(EnumType.STRING)
    private TaxType pricePerUnitTaxType;

    @Enumerated(EnumType.STRING)
    private TaxRate taxRate;

    private Double taxAmount;

    private Double totalAmount;

    @ManyToOne
    @JoinColumn(name = "sale_id")
    private Sale sale;

    private LocalDateTime createdAt;
    private LocalDateTime updateAt;

    public SaleItem() {
    }

    public SaleItem(Long saleItemId, String itemName, String itemHsnCode, String itemDescription, Integer quantity, Unit unit, Double pricePerUnit, TaxType pricePerUnitTaxType, TaxRate taxRate, Double taxAmount, Double totalAmount, Sale sale, LocalDateTime createdAt, LocalDateTime updateAt) {
        this.saleItemId = saleItemId;
        this.itemName = itemName;
        this.itemHsnCode = itemHsnCode;
        this.itemDescription = itemDescription;
        this.quantity = quantity;
        this.unit = unit;
        this.pricePerUnit = pricePerUnit;
        this.pricePerUnitTaxType = pricePerUnitTaxType;
        this.taxRate = taxRate;
        this.taxAmount = taxAmount;
        this.totalAmount = totalAmount;
        this.sale = sale;
        this.createdAt = createdAt;
        this.updateAt = updateAt;
    }

    public Long getSaleItemId() {
        return saleItemId;
    }

    public void setSaleItemId(Long saleItemId) {
        this.saleItemId = saleItemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemHsnCode() {
        return itemHsnCode;
    }

    public void setItemHsnCode(String itemHsnCode) {
        this.itemHsnCode = itemHsnCode;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
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

    public Double getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(Double taxAmount) {
        this.taxAmount = taxAmount;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
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

    public Sale getSale() {
        return sale;
    }

    public void setSale(Sale sale) {
        this.sale = sale;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }
}
