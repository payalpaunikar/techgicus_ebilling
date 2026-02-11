package com.example.techgicus_ebilling.techgicus_ebilling.dto.saleDto;

import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.TaxRate;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.TaxType;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.Unit;

public class SaleItemRequest {
    private Long itemId;
//    private String itemName;
//    private String itemHsnCode;
    private String itemDescription;
    private Double quantity;
  //  private Unit unit;
    private Double pricePerUnit;
    private TaxType pricePerUnitTaxType;
    private TaxRate taxRate;
    private Double taxAmount;
    private Double totalAmount;

    public SaleItemRequest() {
    }





    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
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

    public Double getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(Double taxAmount) {
        this.taxAmount = taxAmount;
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
