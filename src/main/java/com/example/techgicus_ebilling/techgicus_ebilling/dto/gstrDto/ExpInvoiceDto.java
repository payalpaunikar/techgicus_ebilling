package com.example.techgicus_ebilling.techgicus_ebilling.dto.gstrDto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ExpInvoiceDto {

    private String exportType;
    private String invoiceNumber;
    private LocalDate invoiceDate;
    private BigDecimal invoiceValue;
    private String portCode;
    private String shippingBillNumber;
    private LocalDate shippingBillDate;
    private BigDecimal rate;
    private BigDecimal taxableValue;

    public String getExportType() {
        return exportType;
    }

    public void setExportType(String exportType) {
        this.exportType = exportType;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public LocalDate getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(LocalDate invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public BigDecimal getInvoiceValue() {
        return invoiceValue;
    }

    public void setInvoiceValue(BigDecimal invoiceValue) {
        this.invoiceValue = invoiceValue;
    }

    public String getPortCode() {
        return portCode;
    }

    public void setPortCode(String portCode) {
        this.portCode = portCode;
    }

    public String getShippingBillNumber() {
        return shippingBillNumber;
    }

    public void setShippingBillNumber(String shippingBillNumber) {
        this.shippingBillNumber = shippingBillNumber;
    }

    public LocalDate getShippingBillDate() {
        return shippingBillDate;
    }

    public void setShippingBillDate(LocalDate shippingBillDate) {
        this.shippingBillDate = shippingBillDate;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public BigDecimal getTaxableValue() {
        return taxableValue;
    }

    public void setTaxableValue(BigDecimal taxableValue) {
        this.taxableValue = taxableValue;
    }
}
