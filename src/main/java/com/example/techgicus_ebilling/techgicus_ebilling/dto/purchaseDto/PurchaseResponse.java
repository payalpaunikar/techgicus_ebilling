package com.example.techgicus_ebilling.techgicus_ebilling.dto.purchaseDto;

import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.PaymentType;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.State;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.taxDto.ItemTaxSummaryResponse;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.partyDto.PartyResponseDto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PurchaseResponse {

    private Long purchaseId;

    private String billNumber;

    private LocalDate billDate;

    private LocalDate dueDate;
    private State stateOfSupply;

    private PaymentType paymentType;

    private String paymentDescription;

    private Double totalTaxAmount;

    private Double totalAmountWithoutTax;

    private Double deliveryCharges;

    private Double totalAmount;

    private Double sendAmount;

    private Double balance;

    private Boolean isPaid;

    private Boolean isOverdue;

    private PartyResponseDto partyResponseDto;

    List<PurchaseItemResponse> purchaseItemResponses = new ArrayList<>();

    List<PurchasePaymentResponse> purchasePaymentResponses = new ArrayList<>();

    private List<ItemTaxSummaryResponse> taxSummary;


    public PurchaseResponse() {
    }

    public PurchaseResponse(Long purchaseId, String billNumber, LocalDate billDate, LocalDate dueDate, State stateOfSupply, PaymentType paymentType, String paymentDescription, Double totalTaxAmount, Double totalAmountWithoutTax, Double deliveryCharges, Double totalAmount, Double sendAmount, Double balance, Boolean isPaid, Boolean isOverdue, PartyResponseDto partyResponseDto, List<PurchaseItemResponse> purchaseItemResponses, List<PurchasePaymentResponse> purchasePaymentResponses) {
        this.purchaseId = purchaseId;
        this.billNumber = billNumber;
        this.billDate = billDate;
        this.dueDate = dueDate;
        this.stateOfSupply = stateOfSupply;
        this.paymentType = paymentType;
        this.paymentDescription = paymentDescription;
        this.totalTaxAmount = totalTaxAmount;
        this.totalAmountWithoutTax = totalAmountWithoutTax;
        this.deliveryCharges = deliveryCharges;
        this.totalAmount = totalAmount;
        this.sendAmount = sendAmount;
        this.balance = balance;
        this.isPaid = isPaid;
        this.isOverdue = isOverdue;
        this.partyResponseDto = partyResponseDto;
        this.purchaseItemResponses = purchaseItemResponses;
        this.purchasePaymentResponses = purchasePaymentResponses;
    }

    public String getBillNumber() {
        return billNumber;
    }

    public void setBillNumber(String billNumber) {
        this.billNumber = billNumber;
    }

    public LocalDate getBillDate() {
        return billDate;
    }

    public void setBillDate(LocalDate billDate) {
        this.billDate = billDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public State getStateOfSupply() {
        return stateOfSupply;
    }

    public void setStateOfSupply(State stateOfSupply) {
        this.stateOfSupply = stateOfSupply;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public String getPaymentDescription() {
        return paymentDescription;
    }

    public void setPaymentDescription(String paymentDescription) {
        this.paymentDescription = paymentDescription;
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

    public Double getSendAmount() {
        return sendAmount;
    }

    public void setSendAmount(Double sendAmount) {
        this.sendAmount = sendAmount;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public Boolean getIsPaid() {
        return isPaid;
    }

    public void setIsPaid(Boolean isPaid) {
        this.isPaid = isPaid;
    }

    public Boolean getOverdue() {
        return isOverdue;
    }

    public void setOverdue(Boolean isOverdue) {
        this.isOverdue = isOverdue;
    }

    public PartyResponseDto getPartyResponseDto() {
        return partyResponseDto;
    }

    public void setPartyResponseDto(PartyResponseDto partyResponseDto) {
        this.partyResponseDto = partyResponseDto;
    }

    public List<PurchaseItemResponse> getPurchaseItemResponses() {
        return purchaseItemResponses;
    }

    public void setPurchaseItemResponses(List<PurchaseItemResponse> purchaseItemResponses) {
        this.purchaseItemResponses = purchaseItemResponses;
    }

    public List<PurchasePaymentResponse> getPurchasePaymentResponses() {
        return purchasePaymentResponses;
    }

    public void setPurchasePaymentResponses(List<PurchasePaymentResponse> purchasePaymentResponses) {
        this.purchasePaymentResponses = purchasePaymentResponses;
    }

    public Long getPurchaseId() {
        return purchaseId;
    }

    public void setPurchaseId(Long purchaseId) {
        this.purchaseId = purchaseId;
    }

    public List<ItemTaxSummaryResponse> getTaxSummary() {
        return taxSummary;
    }

    public void setTaxSummary(List<ItemTaxSummaryResponse> taxSummary) {
        this.taxSummary = taxSummary;
    }
}
