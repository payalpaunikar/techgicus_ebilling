package com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity;

import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.*;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(
        name = "item",
        indexes = {
                @Index(name = "idx_item_name", columnList = "itemName")
        }
)
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemId;

    private String itemName;

    private String itemHsn;

    private String itemCode;

    private String description;

    @Enumerated(EnumType.STRING)
    private ItemType itemType;

    @Enumerated(EnumType.STRING)
    private Unit baseUnit;

    @Enumerated(EnumType.STRING)
    private Unit secondaryUnit;

    private Double baseUnitToSecondaryUnit;

    private Double salePrice;

    private Double saleDiscountPrice;

    @Enumerated(EnumType.STRING)
    private DiscountType saleDiscountType;

    @Enumerated(EnumType.STRING)
    private TaxType saleTaxType;

    private Double purchasePrice;  // only show for the product item

    @Enumerated(EnumType.STRING)
    private TaxType purchaseTaxType;  // only show for the product item

    @Enumerated(EnumType.STRING)
    private TaxRate taxRate;

     @ManyToOne
     @JoinColumn(name = "company_id")
     private Company company;

     private Double stockOpeningQty;

     private Double stockPrice;

     private LocalDate stockOpeningDate;

     private Double minimumStockToMaintain;

     private String openingStockLocation;


     @ManyToMany
     @JoinTable(
             name = "item_category",
             joinColumns = @JoinColumn(name = "item_id"),
             inverseJoinColumns = @JoinColumn(name = "category_id")
     )
     private Set<Category> categories = new HashSet<>();

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Item() {
    }

    public Item(Long itemId, String itemName, String itemHsn, String itemCode, String description, ItemType itemType, Unit baseUnit, Unit secondaryUnit, Double baseUnitToSecondaryUnit, Double salePrice, Double saleDiscountPrice, DiscountType saleDiscountType, TaxType saleTaxType, Double purchasePrice, TaxType purchaseTaxType, TaxRate taxRate, Company company, Double stockOpeningQty, Double stockPrice, LocalDate stockOpeningDate, Double minimumStockToMaintain, String openingStockLocation, Set<Category> categories, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.itemHsn = itemHsn;
        this.itemCode = itemCode;
        this.description = description;
        this.itemType = itemType;
        this.baseUnit = baseUnit;
        this.secondaryUnit = secondaryUnit;
        this.baseUnitToSecondaryUnit = baseUnitToSecondaryUnit;
        this.salePrice = salePrice;
        this.saleDiscountPrice = saleDiscountPrice;
        this.saleDiscountType = saleDiscountType;
        this.saleTaxType = saleTaxType;
        this.purchasePrice = purchasePrice;
        this.purchaseTaxType = purchaseTaxType;
        this.taxRate = taxRate;
        this.company = company;
        this.stockOpeningQty = stockOpeningQty;
        this.stockPrice = stockPrice;
        this.stockOpeningDate = stockOpeningDate;
        this.minimumStockToMaintain = minimumStockToMaintain;
        this.openingStockLocation = openingStockLocation;
        this.categories = categories;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
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

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public ItemType getItemType() {
        return itemType;
    }

    public void setItemType(ItemType itemType) {
        this.itemType = itemType;
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

    public Double getStockOpeningQty() {
        return stockOpeningQty;
    }

    public void setStockOpeningQty(Double stockOpeningQty) {
        this.stockOpeningQty = stockOpeningQty;
    }

    public Double getStockPrice() {
        return stockPrice;
    }

    public void setStockPrice(Double stockPrice) {
        this.stockPrice = stockPrice;
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

    public Set<Category> getCategories() {
        return categories;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }
}
