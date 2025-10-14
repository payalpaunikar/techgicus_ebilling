package com.example.techgicus_ebilling.techgicus_ebilling.dto.quotationDto;

import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.State;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class QuotationRequest {

    private Long partyId;

    private String referenceNo;

    private LocalDate invoiceDate;

    private State stateOfSupply;

    private String description;

    private Double totalTaxAmount;

    private Double totalAmountWithoutTax;

    private Double deliveryCharges;

    private Double totalAmount;

    private List<QuotationItemRequest> quotationItemRequests = new ArrayList<>();

    public QuotationRequest() {
    }

    public QuotationRequest(Long partyId, String referenceNo, LocalDate invoiceDate, State stateOfSupply, String description, Double totalTaxAmount, Double totalAmountWithoutTax, Double deliveryCharges, Double totalAmount, List<QuotationItemRequest> quotationItemRequests) {
        this.partyId = partyId;
        this.referenceNo = referenceNo;
        this.invoiceDate = invoiceDate;
        this.stateOfSupply = stateOfSupply;
        this.description = description;
        this.totalTaxAmount = totalTaxAmount;
        this.totalAmountWithoutTax = totalAmountWithoutTax;
        this.deliveryCharges = deliveryCharges;
        this.totalAmount = totalAmount;
        this.quotationItemRequests = quotationItemRequests;
    }

    public String getReferenceNo() {
        return referenceNo;
    }

    public void setReferenceNo(String referenceNo) {
        this.referenceNo = referenceNo;
    }

    public LocalDate getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(LocalDate invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public State getStateOfSupply() {
        return stateOfSupply;
    }

    public void setStateOfSupply(State stateOfSupply) {
        this.stateOfSupply = stateOfSupply;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getTotalTaxAmount() {
        return totalTaxAmount;
    }

    public void setTotalTaxAmount(Double totalTaxAmount) {
        this.totalTaxAmount = totalTaxAmount;
    }

    public Double getTotalAmountWithoutTax() {
        return totalAmountWithoutTax;
    }

    public void setTotalAmountWithoutTax(Double totalAmountWithoutTax) {
        this.totalAmountWithoutTax = totalAmountWithoutTax;
    }

    public Double getDeliveryCharges() {
        return deliveryCharges;
    }

    public void setDeliveryCharges(Double deliveryCharges) {
        this.deliveryCharges = deliveryCharges;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public List<QuotationItemRequest> getQuotationItemRequests() {
        return quotationItemRequests;
    }

    public void setQuotationItemRequests(List<QuotationItemRequest> quotationItemRequests) {
        this.quotationItemRequests = quotationItemRequests;
    }

    public Long getPartyId() {
        return partyId;
    }

    public void setPartyId(Long partyId) {
        this.partyId = partyId;
    }
}
