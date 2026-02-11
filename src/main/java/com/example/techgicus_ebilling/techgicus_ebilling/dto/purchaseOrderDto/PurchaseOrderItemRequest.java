package com.example.techgicus_ebilling.techgicus_ebilling.dto.purchaseOrderDto;

import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.TaxRate;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.TaxType;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.Unit;

public class PurchaseOrderItemRequest {

    private Long itemId;

    private String itemName;

    private Integer quantity;

    private Unit unit;

    private Double perUnitRate;

    private TaxType taxType;

    private Double discountAmount;

    private TaxRate taxRate;

    private Double totalTaxAmount;

    private Double totalAmount;

    private String itemDescription;

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
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

    public Double getPerUnitRate() {
        return perUnitRate;
    }

    public void setPerUnitRate(Double perUnitRate) {
        this.perUnitRate = perUnitRate;
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

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }
}
