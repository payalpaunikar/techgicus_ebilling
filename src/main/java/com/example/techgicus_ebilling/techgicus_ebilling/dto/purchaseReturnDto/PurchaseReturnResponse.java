package com.example.techgicus_ebilling.techgicus_ebilling.dto.purchaseReturnDto;

import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.PaymentType;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.State;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.partyDto.PartyResponseDto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PurchaseReturnResponse {

    private Long purchaseReturnId;

    private String returnNo;

    private LocalDate returnDate;

    private String phoneNo;

    private LocalDate billDate;

    private String billNo;

    private Double totalDiscount;

    private Double totalTaxAmount;

    private Integer totalQuantity;

    private Double totalAmount;

    private Double receivedAmount;

    private Double balanceAmount;

    private PaymentType paymentType;

    private State stateOfSupply;

    private String description;

    private PartyResponseDto partyResponseDto;

    private List<PurchaseReturnItemResponse> purchaseReturnItemResponses = new ArrayList<>();

    public Long getPurchaseReturnId() {
        return purchaseReturnId;
    }

    public void setPurchaseReturnId(Long purchaseReturnId) {
        this.purchaseReturnId = purchaseReturnId;
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

    public LocalDate getBillDate() {
        return billDate;
    }

    public void setBillDate(LocalDate billDate) {
        this.billDate = billDate;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
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

    public Integer getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(Integer totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Double getReceivedAmount() {
        return receivedAmount;
    }

    public void setReceivedAmount(Double receivedAmount) {
        this.receivedAmount = receivedAmount;
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

    public List<PurchaseReturnItemResponse> getPurchaseReturnItemResponses() {
        return purchaseReturnItemResponses;
    }

    public void setPurchaseReturnItemResponses(List<PurchaseReturnItemResponse> purchaseReturnItemResponses) {
        this.purchaseReturnItemResponses = purchaseReturnItemResponses;
    }
}
