package com.example.techgicus_ebilling.techgicus_ebilling.dto.deliveryChallanDto;

import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.State;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.TaxRate;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.TaxType;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.Unit;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DeliveryChallanItemResponse {

    private Long deliveryChallanItemId;

    private String name;

    private Integer quantity;

    private Unit unit;

    private Double ratePerUnit;

    private TaxType taxType;

    private Double discountAmount;

    private TaxRate taxRate;

    private Double totalTaxAmount;

    private Double totalAmount;

    public Long getDeliveryChallanItemId() {
        return deliveryChallanItemId;
    }

    public void setDeliveryChallanItemId(Long deliveryChallanItemId) {
        this.deliveryChallanItemId = deliveryChallanItemId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
