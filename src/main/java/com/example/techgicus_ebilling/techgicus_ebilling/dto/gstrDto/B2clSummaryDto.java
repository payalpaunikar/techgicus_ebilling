package com.example.techgicus_ebilling.techgicus_ebilling.dto.gstrDto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class B2clSummaryDto {
    private int noOfInvoices;
    private BigDecimal totalInvoiceValue;
    private BigDecimal totalTaxableValue;
    private BigDecimal totalCess;

    // Detailed list if needed
    private List<B2clInvoiceDto> invoices = new ArrayList<>();

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

    public BigDecimal getTotalTaxableValue() {
        return totalTaxableValue;
    }

    public void setTotalTaxableValue(BigDecimal totalTaxableValue) {
        this.totalTaxableValue = totalTaxableValue;
    }

    public BigDecimal getTotalCess() {
        return totalCess;
    }

    public void setTotalCess(BigDecimal totalCess) {
        this.totalCess = totalCess;
    }

    public List<B2clInvoiceDto> getInvoices() {
        return invoices;
    }

    public void setInvoices(List<B2clInvoiceDto> invoices) {
        this.invoices = invoices;
    }
}
