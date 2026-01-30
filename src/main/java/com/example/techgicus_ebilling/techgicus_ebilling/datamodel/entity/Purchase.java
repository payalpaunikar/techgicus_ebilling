package com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity;

import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.PaymentType;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.State;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
public class Purchase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long purchaseId;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @ManyToOne
    @JoinColumn(name = "party_id")
    private Party party;

    private String billNumber;

    private LocalDate billDate;

    private LocalDate dueDate;

    @Enumerated(EnumType.STRING)
    private State stateOfSupply;

    @Enumerated(EnumType.STRING)
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


    @OneToMany(mappedBy = "purchase",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<PurchaseItem> purchaseItems = new ArrayList<>();

    @OneToMany(mappedBy = "purchase",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<PurchasePayment> purchasePayments = new ArrayList<>();

    private LocalDateTime createdAt;
    private LocalDateTime updateAt;

    public Purchase() {
    }

    public Purchase(Long purchaseId, Company company, Party party, String billNumber, LocalDate billDate, LocalDate dueDate, State stateOfSupply, PaymentType paymentType, String paymentDescription, Double totalTaxAmount, Double totalAmountWithoutTax, Double deliveryCharges, Double totalAmount, Double sendAmount, Double balance, Boolean isPaid, Boolean isOverdue, List<PurchaseItem> purchaseItems, List<PurchasePayment> purchasePayments, LocalDateTime createdAt, LocalDateTime updateAt) {
        this.purchaseId = purchaseId;
        this.company = company;
        this.party = party;
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
        this.purchaseItems = purchaseItems;
        this.purchasePayments = purchasePayments;
        this.createdAt = createdAt;
        this.updateAt = updateAt;
    }

    public Long getPurchaseId() {
        return purchaseId;
    }

    public void setPurchaseId(Long purchaseId) {
        this.purchaseId = purchaseId;
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

    public Boolean getIsOverdue() {
        return isOverdue;
    }

    public void setOverdue(Boolean isOverdue) {
        this.isOverdue = isOverdue;
    }

    public List<PurchaseItem> getPurchaseItems() {
        return purchaseItems;
    }

    public void setPurchaseItems(List<PurchaseItem> purchaseItems) {
        this.purchaseItems = purchaseItems;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(LocalDateTime updateAt) {
        this.updateAt = updateAt;
    }

    public List<PurchasePayment> getPurchasePayments() {
        return purchasePayments;
    }

    public void setPurchasePayments(List<PurchasePayment> purchasePayments) {
        this.purchasePayments = purchasePayments;
    }
}
