package com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity;

import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.TaxRate;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.TaxType;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.Unit;
import jakarta.persistence.*;
import software.amazon.awssdk.services.s3.endpoints.internal.Value;


@Entity
public class DeliveryChallanItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long deliveryChallanItemId;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

//    private String name;

    private Integer quantity;

    @Enumerated(EnumType.STRING)
    private Unit unit;

    private Double ratePerUnit;

    @Enumerated(EnumType.STRING)
    private TaxType taxType;

    private Double discountAmount;

    @Enumerated(EnumType.STRING)
    private TaxRate taxRate;

    private Double totalTaxAmount;

    private Double totalAmount;

    @ManyToOne
    @JoinColumn(name = "delivery_challan_id")
    private DeliveryChallan deliveryChallan;

    public Long getDeliveryChallanItemId() {
        return deliveryChallanItemId;
    }

    public void setDeliveryChallanItemId(Long deliveryChallanItemId) {
        this.deliveryChallanItemId = deliveryChallanItemId;
    }

//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }

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

    public DeliveryChallan getDeliveryChallan() {
        return deliveryChallan;
    }

    public void setDeliveryChallan(DeliveryChallan deliveryChallan) {
        this.deliveryChallan = deliveryChallan;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }
}
