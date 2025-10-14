package com.example.techgicus_ebilling.techgicus_ebilling.dto.saleDto;

import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.PaymentType;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.SaleType;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.State;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class SaleRequest {
    private Long partyId;
    private String billingAddress;
    private String shippingAddress;
    private String invoiceNumber;
    private LocalDate invoceDate;
    private LocalDate dueDate;
    private SaleType saleType;
    private State stateOfSupply;
    private PaymentType paymentType;
    private String paymentDescription;
    private Double totalAmountWithoutTax; // total before tax
    private Double totalTaxAmount;
    private Double deliveryCharges;
//    private Double roundOffAmount;
    private Double totalAmount;
    private Double receivedAmount;
    private Double balance;
    private Boolean isPaid;
    private Boolean isOverdue;

    private List<SaleItemRequest> saleItems;


    public SaleRequest() {
    }

    public SaleRequest(Long partyId, String billingAddress, String shippingAddress, String invoiceNumber, LocalDate invoceDate, LocalDate dueDate, SaleType saleType, State stateOfSupply, PaymentType paymentType, String paymentDescription, Double totalAmountWithoutTax, Double totalTaxAmount, Double deliveryCharges, Double totalAmount, Double receivedAmount, Double balance, Boolean isPaid, Boolean isOverdue, List<SaleItemRequest> saleItems) {
        this.partyId = partyId;
        this.billingAddress = billingAddress;
        this.shippingAddress = shippingAddress;
        this.invoiceNumber = invoiceNumber;
        this.invoceDate = invoceDate;
        this.dueDate = dueDate;
        this.saleType = saleType;
        this.stateOfSupply = stateOfSupply;
        this.paymentType = paymentType;
        this.paymentDescription = paymentDescription;
        this.totalAmountWithoutTax = totalAmountWithoutTax;
        this.totalTaxAmount = totalTaxAmount;
        this.deliveryCharges = deliveryCharges;
        this.totalAmount = totalAmount;
        this.receivedAmount = receivedAmount;
        this.balance = balance;
        this.isPaid = isPaid;
        this.isOverdue = isOverdue;
        this.saleItems = saleItems;
    }

    public Long getPartyId() {
        return partyId;
    }

    public void setPartyId(Long partyId) {
        this.partyId = partyId;
    }

    public String getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(String billingAddress) {
        this.billingAddress = billingAddress;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }



    public SaleType getSaleType() {
        return saleType;
    }

    public void setSaleType(SaleType saleType) {
        this.saleType = saleType;
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

    public Double getDeliveryCharges() {
        return deliveryCharges;
    }

    public void setDeliveryCharges(Double deliveryCharges) {
        this.deliveryCharges = deliveryCharges;
    }

    public List<SaleItemRequest> getSaleItems() {
        return saleItems;
    }

    public void setSaleItems(List<SaleItemRequest> saleItems) {
        this.saleItems = saleItems;
    }

    public LocalDate getInvoceDate() {
        return invoceDate;
    }

    public void setInvoceDate(LocalDate invoceDate) {
        this.invoceDate = invoceDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public Double getTotalAmountWithoutTax() {
        return totalAmountWithoutTax;
    }

    public void setTotalAmountWithoutTax(Double totalAmountWithoutTax) {
        this.totalAmountWithoutTax = totalAmountWithoutTax;
    }

    public Double getTotalTaxAmount() {
        return totalTaxAmount;
    }

    public void setTotalTaxAmount(Double totalTaxAmount) {
        this.totalTaxAmount = totalTaxAmount;
    }

//    public Double getRoundOffAmount() {
//        return roundOffAmount;
//    }
//
//    public void setRoundOffAmount(Double roundOffAmount) {
//        this.roundOffAmount = roundOffAmount;
//    }

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

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public Boolean getPaid() {
        return isPaid;
    }

    public void setPaid(Boolean paid) {
        isPaid = paid;
    }

    public Boolean getOverdue() {
        return isOverdue;
    }

    public void setOverdue(Boolean overdue) {
        isOverdue = overdue;
    }
}
