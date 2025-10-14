package com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity;


import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.TaxRate;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.TaxType;
import jakarta.persistence.*;

@Entity
public class ExpenseItem {

     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     private Long expenseItemId;

     private String name;

     private String hsnCode;

     private Double price;

     @Enumerated(EnumType.STRING)
     private TaxType priceTaxType;

     private TaxRate taxRate;

     @ManyToOne
     @JoinColumn(name = "company_id")
     private Company company;

    public ExpenseItem() {
    }

    public ExpenseItem(Long expenseItemId, String name, String hsnCode, Double price, TaxType priceTaxType, TaxRate taxRate, Company company) {
        this.expenseItemId = expenseItemId;
        this.name = name;
        this.hsnCode = hsnCode;
        this.price = price;
        this.priceTaxType = priceTaxType;
        this.taxRate = taxRate;
        this.company = company;
    }

    public Long getExpenseItemId() {
        return expenseItemId;
    }

    public void setExpenseItemId(Long expenseItemId) {
        this.expenseItemId = expenseItemId;
    }

    public String getHsnCode() {
        return hsnCode;
    }

    public void setHsnCode(String hsnCode) {
        this.hsnCode = hsnCode;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public TaxType getPriceTaxType() {
        return priceTaxType;
    }

    public void setPriceTaxType(TaxType priceTaxType) {
        this.priceTaxType = priceTaxType;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
