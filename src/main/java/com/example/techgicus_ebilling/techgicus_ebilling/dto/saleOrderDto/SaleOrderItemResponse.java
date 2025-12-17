package com.example.techgicus_ebilling.techgicus_ebilling.dto.saleOrderDto;

import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.TaxRate;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.TaxType;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.Unit;

public class SaleOrderItemResponse {

    private Long saleOrderItemId;

    private Long itemId;

    private String name;

    private Double quantity;
    private Unit unit;

    private Double pricePerRate;

    private TaxType taxType;

    private Double discountAmount;

    private TaxRate taxRate;

    private Double totalTaxAmount;

    private Double totalAmount;

    public Long getSaleOrderItemId() {
        return saleOrderItemId;
    }

    public void setSaleOrderItemId(Long saleOrderItemId) {
        this.saleOrderItemId = saleOrderItemId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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

    public Double getPricePerRate() {
        return pricePerRate;
    }

    public void setPricePerRate(Double pricePerRate) {
        this.pricePerRate = pricePerRate;
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
}
