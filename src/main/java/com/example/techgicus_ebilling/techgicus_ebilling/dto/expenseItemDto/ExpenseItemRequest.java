package com.example.techgicus_ebilling.techgicus_ebilling.dto.expenseItemDto;

import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.TaxRate;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.TaxType;

public class ExpenseItemRequest {

    private String name;

    private String hsnCode;

    private Double price;

    private TaxType priceTaxType;

    private TaxRate taxRate;

    public ExpenseItemRequest() {
    }

    public ExpenseItemRequest(String name, String hsnCode, Double price, TaxType priceTaxType, TaxRate taxRate) {
        this.name = name;
        this.hsnCode = hsnCode;
        this.price = price;
        this.priceTaxType = priceTaxType;
        this.taxRate = taxRate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
