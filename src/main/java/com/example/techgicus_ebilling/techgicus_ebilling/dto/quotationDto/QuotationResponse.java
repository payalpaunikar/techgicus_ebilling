package com.example.techgicus_ebilling.techgicus_ebilling.dto.quotationDto;

import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.QuotationType;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.State;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.partyDto.PartyResponseDto;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.taxDto.ItemTaxSummaryResponse;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class QuotationResponse {
    private Long quotationId;

    private String referenceNo;

    private LocalDate invoiceDate;

    private State stateOfSupply;

    private String description;

    private Double totalTaxAmount;

    private Double totalAmountWithoutTax;

    private Double deliveryCharges;

    private Double totalAmount;

    private QuotationType quotationType;

    private PartyResponseDto partyResponseDto;
    private List<ItemTaxSummaryResponse> taxSummary;


    private List<QuotationItemResponse> quotationItemResponses = new ArrayList<>();

    public QuotationResponse() {
    }

    public QuotationResponse(Long quotationId, String referenceNo, LocalDate invoiceDate, State stateOfSupply, String description, Double totalTaxAmount, Double totalAmountWithoutTax, Double deliveryCharges, Double totalAmount, QuotationType quotationType, PartyResponseDto partyResponseDto, List<QuotationItemResponse> quotationItemResponses) {
        this.quotationId = quotationId;
        this.referenceNo = referenceNo;
        this.invoiceDate = invoiceDate;
        this.stateOfSupply = stateOfSupply;
        this.description = description;
        this.totalTaxAmount = totalTaxAmount;
        this.totalAmountWithoutTax = totalAmountWithoutTax;
        this.deliveryCharges = deliveryCharges;
        this.totalAmount = totalAmount;
        this.quotationType = quotationType;
        this.partyResponseDto = partyResponseDto;
        this.quotationItemResponses = quotationItemResponses;
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

    public PartyResponseDto getPartyResponseDto() {
        return partyResponseDto;
    }

    public void setPartyResponseDto(PartyResponseDto partyResponseDto) {
        this.partyResponseDto = partyResponseDto;
    }

    public List<QuotationItemResponse> getQuotationItemResponses() {
        return quotationItemResponses;
    }

    public void setQuotationItemResponses(List<QuotationItemResponse> quotationItemResponses) {
        this.quotationItemResponses = quotationItemResponses;
    }

    public List<ItemTaxSummaryResponse> getTaxSummary() {
        return taxSummary;
    }

    public void setTaxSummary(List<ItemTaxSummaryResponse> taxSummary) {
        this.taxSummary = taxSummary;
    }

    public Long getQuotationId() {
        return quotationId;
    }

    public void setQuotationId(Long quotationId) {
        this.quotationId = quotationId;
    }

    public QuotationType getQuotationType() {
        return quotationType;
    }

    public void setQuotationType(QuotationType quotationType) {
        this.quotationType = quotationType;
    }
}
