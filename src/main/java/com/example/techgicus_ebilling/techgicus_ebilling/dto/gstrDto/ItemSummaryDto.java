package com.example.techgicus_ebilling.techgicus_ebilling.dto.gstrDto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ItemSummaryDto {
    private int noOfHsn;
    private BigDecimal totalValue;
    private BigDecimal totalTaxableValue;
    private BigDecimal totalIntegratedTax;
    private BigDecimal totalCentralTax;
    private BigDecimal totalStateUtTax;
    private BigDecimal totalCess;

    // Detailed item lines
    private List<ItemHsnDetailDto> itemDetails = new ArrayList<>();

    public int getNoOfHsn() {
        return noOfHsn;
    }

    public void setNoOfHsn(int noOfHsn) {
        this.noOfHsn = noOfHsn;
    }

    public BigDecimal getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(BigDecimal totalValue) {
        this.totalValue = totalValue;
    }

    public BigDecimal getTotalTaxableValue() {
        return totalTaxableValue;
    }

    public void setTotalTaxableValue(BigDecimal totalTaxableValue) {
        this.totalTaxableValue = totalTaxableValue;
    }

    public BigDecimal getTotalIntegratedTax() {
        return totalIntegratedTax;
    }

    public void setTotalIntegratedTax(BigDecimal totalIntegratedTax) {
        this.totalIntegratedTax = totalIntegratedTax;
    }

    public BigDecimal getTotalCentralTax() {
        return totalCentralTax;
    }

    public void setTotalCentralTax(BigDecimal totalCentralTax) {
        this.totalCentralTax = totalCentralTax;
    }

    public BigDecimal getTotalStateUtTax() {
        return totalStateUtTax;
    }

    public void setTotalStateUtTax(BigDecimal totalStateUtTax) {
        this.totalStateUtTax = totalStateUtTax;
    }

    public BigDecimal getTotalCess() {
        return totalCess;
    }

    public void setTotalCess(BigDecimal totalCess) {
        this.totalCess = totalCess;
    }

    public List<ItemHsnDetailDto> getItemDetails() {
        return itemDetails;
    }

    public void setItemDetails(List<ItemHsnDetailDto> itemDetails) {
        this.itemDetails = itemDetails;
    }
}
