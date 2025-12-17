package com.example.techgicus_ebilling.techgicus_ebilling.dto.itemDto;

import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.*;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.categoryDto.CategoryResponse;

import java.time.LocalDate;
import java.util.Set;

public class ItemResponse {
    private Long itemId;

    private String itemName;

    private String itemHsn;

    private String itemCode;

    private String description;

    private ItemType itemType;

    private Unit baseUnit;

    private Unit secondaryUnit;

    private Double baseUnitToSecondaryUnit;

    private Double salePrice;

    private Double saleDiscountPrice;

    private DiscountType saleDiscountType;

    private TaxType saleTaxType;

    private Double purchasePrice;

    private TaxType purchaseTaxType;

    private TaxRate taxRate;

    private Double stockOpeningQty;

    private Double stockPricePerQty;

    private LocalDate stockOpeningDate;

    private Double minimumStockToMaintain;

    private String openingStockLocation;

    private Double totalStockIn;
    private Double reservedStock;  // items allocated but not yet sold (like pending orders)
    private Double availableStock;
    private Double stockValue;

    private Set<CategoryResponse> categories;

    public ItemResponse() {
    }

    public Double getTotalStockIn() {
        return totalStockIn;
    }

    public void setTotalStockIn(Double totalStockIn) {
        this.totalStockIn = totalStockIn;
    }

    public Double getReservedStock() {
        return reservedStock;
    }

    public void setReservedStock(Double reservedStock) {
        this.reservedStock = reservedStock;
    }

    public Double getAvailableStock() {
        return availableStock;
    }

    public void setAvailableStock(Double availableStock) {
        this.availableStock = availableStock;
    }

    public Double getStockValue() {
        return stockValue;
    }

    public void setStockValue(Double stockValue) {
        this.stockValue = stockValue;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
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

    public Double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(Double salePrice) {
        this.salePrice = salePrice;
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

    public TaxType getSaleTaxType() {
        return saleTaxType;
    }

    public void setSaleTaxType(TaxType saleTaxType) {
        this.saleTaxType = saleTaxType;
    }

    public Double getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(Double purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public TaxType getPurchaseTaxType() {
        return purchaseTaxType;
    }

    public void setPurchaseTaxType(TaxType purchaseTaxType) {
        this.purchaseTaxType = purchaseTaxType;
    }

    public TaxRate getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(TaxRate taxRate) {
        this.taxRate = taxRate;
    }



    public Set<CategoryResponse> getCategories() {
        return categories;
    }

    public void setCategories(Set<CategoryResponse> categories) {
        this.categories = categories;
    }

    public Double getStockOpeningQty() {
        return stockOpeningQty;
    }

    public void setStockOpeningQty(Double stockOpeningQty) {
        this.stockOpeningQty = stockOpeningQty;
    }

    public Double getStockPricePerQty() {
        return stockPricePerQty;
    }

    public void setStockPricePerQty(Double stockPricePerQty) {
        this.stockPricePerQty = stockPricePerQty;
    }

    public LocalDate getStockOpeningDate() {
        return stockOpeningDate;
    }

    public void setStockOpeningDate(LocalDate stockOpeningDate) {
        this.stockOpeningDate = stockOpeningDate;
    }

    public Double getMinimumStockToMaintain() {
        return minimumStockToMaintain;
    }

    public void setMinimumStockToMaintain(Double minimumStockToMaintain) {
        this.minimumStockToMaintain = minimumStockToMaintain;
    }

    public String getOpeningStockLocation() {
        return openingStockLocation;
    }

    public void setOpeningStockLocation(String openingStockLocation) {
        this.openingStockLocation = openingStockLocation;
    }
}

