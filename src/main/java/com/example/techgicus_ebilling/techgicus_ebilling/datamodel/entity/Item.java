package com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity;

import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.*;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
    @Column(length = 50)
    private Unit baseUnit;

    @Enumerated(EnumType.STRING)
    @Column(length = 50)
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

     private Double stockPricePerQty;

     private LocalDate stockOpeningDate;

     private Double minimumStockToMaintain;

     private String openingStockLocation;


  //  private Double totalStockIn = 0.0;
  //  private Double reservedStock =0.0;  // items allocated but not yet sold (like pending orders)
    private Double availableStock = 0.0;
    private Double stockValue = 0.0;


     @ManyToMany
     @JoinTable(
             name = "item_category",
             joinColumns = @JoinColumn(name = "item_id"),
             inverseJoinColumns = @JoinColumn(name = "category_id")
     )
     private Set<Category> categories = new HashSet<>();


     @OneToMany(mappedBy = "item",cascade = CascadeType.ALL,orphanRemoval = true)
     List<StockTransaction> stockTransactions = new ArrayList<>();

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Item() {
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

    public Set<Category> getCategories() {
        return categories;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }

//    public Double getTotalStockIn() {
//        return totalStockIn;
//    }
//
//    public void setTotalStockIn(Double totalStockIn) {
//        this.totalStockIn = totalStockIn;
//    }
//
//    public Double getReservedStock() {
//        return reservedStock;
//    }
//
//    public void setReservedStock(Double reservedStock) {
//        this.reservedStock = reservedStock;
//    }

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

    public List<StockTransaction> getStockTransactions() {
        return stockTransactions;
    }

    public void setStockTransactions(List<StockTransaction> stockTransactions) {
        this.stockTransactions = stockTransactions;
    }
}
