package com.example.techgicus_ebilling.techgicus_ebilling.dto.saleDto;

import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.PaymentType;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.SaleType;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.State;

import java.time.LocalDateTime;
import java.util.List;

public class SaleRequest {
    private String billingAddress;
    private String shippingAddress;
    private String invoiceNumber;
    private LocalDateTime dueDateAndTime;
    private SaleType saleType;
    private State stateOfSupply;
    private PaymentType paymentType;
    private String paymentDescription;
    private Double deliveryCharges;

    private List<SaleItemRequest> saleItems;


    public SaleRequest() {
    }

    public SaleRequest(String billingAddress, String shippingAddress, String invoiceNumber, LocalDateTime dueDateAndTime, SaleType saleType, State stateOfSupply, PaymentType paymentType, String paymentDescription, Double deliveryCharges, List<SaleItemRequest> saleItems) {
        this.billingAddress = billingAddress;
        this.shippingAddress = shippingAddress;
        this.invoiceNumber = invoiceNumber;
        this.dueDateAndTime = dueDateAndTime;
        this.saleType = saleType;
        this.stateOfSupply = stateOfSupply;
        this.paymentType = paymentType;
        this.paymentDescription = paymentDescription;
        this.deliveryCharges = deliveryCharges;
        this.saleItems = saleItems;
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

    public LocalDateTime getDueDateAndTime() {
        return dueDateAndTime;
    }

    public void setDueDateAndTime(LocalDateTime dueDateAndTime) {
        this.dueDateAndTime = dueDateAndTime;
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
}
