package com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity;

import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.TaxRate;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.TaxType;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.Unit;
import jakarta.persistence.*;


@Entity
public class QuotationItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long quotationItemId;

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

    private Double totalTaxAmount;

    private Double totalAmount;

    @ManyToOne
    @JoinColumn(name = "quotation_id")
    private Quotation quotation;

    public QuotationItem() {
    }

    public QuotationItem(Long quotationItemId, String itemName, String itemHsnCode, String itemDescription, Integer quantity, Unit unit, Double pricePerUnit, TaxType pricePerUnitTaxType, TaxRate taxRate, Double totalTaxAmount, Double totalAmount, Quotation quotation) {
        this.quotationItemId = quotationItemId;
        this.itemName = itemName;
        this.itemHsnCode = itemHsnCode;
        this.itemDescription = itemDescription;
        this.quantity = quantity;
        this.unit = unit;
        this.pricePerUnit = pricePerUnit;
        this.pricePerUnitTaxType = pricePerUnitTaxType;
        this.taxRate = taxRate;
        this.totalTaxAmount = totalTaxAmount;
        this.totalAmount = totalAmount;
        this.quotation = quotation;
    }

    public Long getQuotationItemId() {
        return quotationItemId;
    }

    public void setQuotationItemId(Long quotationItemId) {
        this.quotationItemId = quotationItemId;
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

    public Quotation getQuotation() {
        return quotation;
    }

    public void setQuotation(Quotation quotation) {
        this.quotation = quotation;
    }
}
