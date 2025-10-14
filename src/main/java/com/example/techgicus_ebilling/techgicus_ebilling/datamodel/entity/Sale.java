package com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity;

import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.PaymentType;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.SaleType;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.State;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Entity
public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long saleId;

    private String billingAddress;
    private String shippingAddress;

    private String invoiceNumber;

    private LocalDate invoceDate;
    private LocalDate dueDate;

    @Enumerated(EnumType.STRING)
    private SaleType saleType;

    @Enumerated(EnumType.STRING)
    private State stateOfSupply;

    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

    private String paymentDescription;

    private Double totalAmountWithoutTax; // total before tax

    private Double totalTaxAmount;

    private Double deliveryCharges;

  //  private Double roundOffAmount;

    private Double totalAmount;

    private Double receivedAmount;

    private Double balance;

    private Boolean isPaid;

    private Boolean isOverdue;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @ManyToOne
    @JoinColumn(name = "party_id")
    private Party party;

    @OneToMany(mappedBy = "sale",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<SaleItem> saleItem = new ArrayList<>();

    @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SalePayment> salePayments = new ArrayList<>();


    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Sale() {
    }

    public Sale(Long saleId, String billingAddress, String shippingAddress, String invoiceNumber, LocalDate invoceDate, LocalDate dueDate, SaleType saleType, State stateOfSupply, PaymentType paymentType, String paymentDescription, Double totalAmountWithoutTax, Double totalTaxAmount, Double deliveryCharges, Double totalAmount, Double receivedAmount, Double balance, Boolean isPaid, Boolean isOverdue, Company company, Party party, List<SaleItem> saleItem, List<SalePayment> salePayments, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.saleId = saleId;
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
        this.company = company;
        this.party = party;
        this.saleItem = saleItem;
        this.salePayments = salePayments;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getSaleId() {
        return saleId;
    }

    public void setSaleId(Long saleId) {
        this.saleId = saleId;
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

    public Double getDeliveryCharges() {
        return deliveryCharges;
    }

    public void setDeliveryCharges(Double deliveryCharges) {
        this.deliveryCharges = deliveryCharges;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Party getParty() {
        return party;
    }

    public void setParty(Party party) {
        this.party = party;
    }

    public List<SaleItem> getSaleItem() {
        return saleItem;
    }

    public void setSaleItem(List<SaleItem> saleItem) {
        this.saleItem = saleItem;
    }

    public List<SalePayment> getSalePayments() {
        return salePayments;
    }

    public void setSalePayments(List<SalePayment> salePayments) {
        this.salePayments = salePayments;
    }
}
