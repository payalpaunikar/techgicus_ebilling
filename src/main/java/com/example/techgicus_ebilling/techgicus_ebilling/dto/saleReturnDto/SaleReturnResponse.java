package com.example.techgicus_ebilling.techgicus_ebilling.dto.saleReturnDto;

import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.PaymentType;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.State;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.partyDto.PartyResponseDto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SaleReturnResponse {

    private Long saleReturnId;

    private String returnNo;

    private LocalDate returnDate;

    private String phoneNo;

    private LocalDate invoiceDate;

    private String invoiceNo;

    private Double totalDiscount;

    private Double totalTaxAmount;

    private Double totalQuantity;

    private Double totalAmount;

    private Double paidAmount;

    private Double balanceAmount;

    private PaymentType paymentType;

    private State stateOfSupply;

    private String description;

    private PartyResponseDto partyResponseDto;

    private List<SaleReturnItemResponse> saleReturnItemResponses = new ArrayList<>();

    public Long getSaleReturnId() {
        return saleReturnId;
    }

    public void setSaleReturnId(Long saleReturnId) {
        this.saleReturnId = saleReturnId;
    }

    public String getReturnNo() {
        return returnNo;
    }

    public void setReturnNo(String returnNo) {
        this.returnNo = returnNo;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public LocalDate getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(LocalDate invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public Double getTotalDiscount() {
        return totalDiscount;
    }

    public void setTotalDiscount(Double totalDiscount) {
        this.totalDiscount = totalDiscount;
    }

    public Double getTotalTaxAmount() {
        return totalTaxAmount;
    }

    public void setTotalTaxAmount(Double totalTaxAmount) {
        this.totalTaxAmount = totalTaxAmount;
    }

    public Double getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(Double totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Double getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(Double paidAmount) {
        this.paidAmount = paidAmount;
    }

    public Double getBalanceAmount() {
        return balanceAmount;
    }

    public void setBalanceAmount(Double balanceAmount) {
        this.balanceAmount = balanceAmount;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
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

    public PartyResponseDto getPartyResponseDto() {
        return partyResponseDto;
    }

    public void setPartyResponseDto(PartyResponseDto partyResponseDto) {
        this.partyResponseDto = partyResponseDto;
    }

    public List<SaleReturnItemResponse> getSaleReturnItemResponses() {
        return saleReturnItemResponses;
    }

    public void setSaleReturnItemResponses(List<SaleReturnItemResponse> saleReturnItemResponses) {
        this.saleReturnItemResponses = saleReturnItemResponses;
    }
}
