package com.example.techgicus_ebilling.techgicus_ebilling.dto.itemDto;

import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.*;

import java.time.LocalDate;
import java.util.Set;

public class CreatedServiceItem {

    private String itemName;

    private String itemHsn;

    private String itemCode;

    private String description;

    private ItemType itemType;

    private Unit baseUnit;

    private Unit secondaryUnit;

    private Double baseUnitToSecondaryUnit;

    private Set<Long> categoryIds;

    private Double salePrice;

    private TaxType saleTaxType;

    private TaxRate taxRate;

    private Double saleDiscountPrice;

    private DiscountType saleDiscountType;


    public CreatedServiceItem() {
    }

    public CreatedServiceItem(String itemName, String itemHsn, String itemCode, String description, ItemType itemType, Unit baseUnit, Unit secondaryUnit, Double baseUnitToSecondaryUnit, Set<Long> categoryIds, Double salePrice, TaxType saleTaxType, TaxRate taxRate, Double saleDiscountPrice, DiscountType saleDiscountType) {
        this.itemName = itemName;
        this.itemHsn = itemHsn;
        this.itemCode = itemCode;
        this.description = description;
        this.itemType = itemType;
        this.baseUnit = baseUnit;
        this.secondaryUnit = secondaryUnit;
        this.baseUnitToSecondaryUnit = baseUnitToSecondaryUnit;
        this.categoryIds = categoryIds;
        this.salePrice = salePrice;
        this.saleTaxType = saleTaxType;
        this.taxRate = taxRate;
        this.saleDiscountPrice = saleDiscountPrice;
        this.saleDiscountType = saleDiscountType;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }


    public String getItemHsn() {
        return itemHsn;
    }

    public void setItemHsn(String itemHsn) {
        this.itemHsn = itemHsn;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ItemType getItemType() {
        return itemType;
    }

    public void setItemType(ItemType itemType) {
        this.itemType = itemType;
    }

    public Unit getBaseUnit() {
        return baseUnit;
    }

    public void setBaseUnit(Unit baseUnit) {
        this.baseUnit = baseUnit;
    }

    public Unit getSecondaryUnit() {
        return secondaryUnit;
    }

    public void setSecondaryUnit(Unit secondaryUnit) {
        this.secondaryUnit = secondaryUnit;
    }

    public Double getBaseUnitToSecondaryUnit() {
        return baseUnitToSecondaryUnit;
    }

    public void setBaseUnitToSecondaryUnit(Double baseUnitToSecondaryUnit) {
        this.baseUnitToSecondaryUnit = baseUnitToSecondaryUnit;
    }

    public Set<Long> getCategoryIds() {
        return categoryIds;
    }

    public void setCategoryIds(Set<Long> categoryIds) {
        this.categoryIds = categoryIds;
    }

    public Double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(Double salePrice) {
        this.salePrice = salePrice;
    }

    public TaxType getSaleTaxType() {
        return saleTaxType;
    }

    public void setSaleTaxType(TaxType saleTaxType) {
        this.saleTaxType = saleTaxType;
    }

    public TaxRate getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(TaxRate taxRate) {
        this.taxRate = taxRate;
    }

    public Double getSaleDiscountPrice() {
        return saleDiscountPrice;
    }

    public void setSaleDiscountPrice(Double saleDiscountPrice) {
        this.saleDiscountPrice = saleDiscountPrice;
    }

    public DiscountType getSaleDiscountType() {
        return saleDiscountType;
    }

    public void setSaleDiscountType(DiscountType saleDiscountType) {
        this.saleDiscountType = saleDiscountType;
    }
}
