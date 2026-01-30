package com.example.techgicus_ebilling.techgicus_ebilling.dto.gstrDto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ExpSummaryDto {
    private int noOfInvoices;
    private BigDecimal totalInvoiceValue;
    private int noOfShippingBill;
    private BigDecimal totalTaxableValue;

    // Detailed list
    private List<ExpInvoiceDto> invoices = new ArrayList<>();

    public int getNoOfInvoices() {
        return noOfInvoices;
    }

    public void setNoOfInvoices(int noOfInvoices) {
        this.noOfInvoices = noOfInvoices;
    }

    public BigDecimal getTotalInvoiceValue() {
        return totalInvoiceValue;
    }

    public void setTotalInvoiceValue(BigDecimal totalInvoiceValue) {
        this.totalInvoiceValue = totalInvoiceValue;
    }

    public int getNoOfShippingBill() {
        return noOfShippingBill;
    }

    public void setNoOfShippingBill(int noOfShippingBill) {
        this.noOfShippingBill = noOfShippingBill;
    }

    public BigDecimal getTotalTaxableValue() {
        return totalTaxableValue;
    }

    public void setTotalTaxableValue(BigDecimal totalTaxableValue) {
        this.totalTaxableValue = totalTaxableValue;
    }

    public List<ExpInvoiceDto> getInvoices() {
        return invoices;
    }

    public void setInvoices(List<ExpInvoiceDto> invoices) {
        this.invoices = invoices;
    }
}
