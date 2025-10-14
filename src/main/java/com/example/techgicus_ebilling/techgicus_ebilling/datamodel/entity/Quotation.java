package com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity;

import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.PaymentType;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.QuotationType;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.State;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
public class Quotation {

     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     private Long quotationId;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @ManyToOne
    @JoinColumn(name = "party_id")
    private Party party;

    private String referenceNo;

    private LocalDate invoiceDate;

    private State stateOfSupply;

    private String description;

    private Double totalTaxAmount;

    private Double totalAmountWithoutTax;

    private Double deliveryCharges;

    private Double totalAmount;

    @OneToMany(mappedBy = "quotation",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<QuotationItem> quotationItems = new ArrayList<>();


    @Enumerated(EnumType.STRING)
    private QuotationType quotationType;

    private LocalDateTime createdAt;

    private LocalDateTime updateAt;

    public Quotation() {
    }

    public Quotation(Long quotationId, Company company, Party party, String referenceNo, LocalDate invoiceDate, State stateOfSupply, String description, Double totalTaxAmount, Double totalAmountWithoutTax, Double deliveryCharges, Double totalAmount, List<QuotationItem> quotationItems, QuotationType quotationType, LocalDateTime createdAt, LocalDateTime updateAt) {
        this.quotationId = quotationId;
        this.company = company;
        this.party = party;
        this.referenceNo = referenceNo;
        this.invoiceDate = invoiceDate;
        this.stateOfSupply = stateOfSupply;
        this.description = description;
        this.totalTaxAmount = totalTaxAmount;
        this.totalAmountWithoutTax = totalAmountWithoutTax;
        this.deliveryCharges = deliveryCharges;
        this.totalAmount = totalAmount;
        this.quotationItems = quotationItems;
        this.quotationType = quotationType;
        this.createdAt = createdAt;
        this.updateAt = updateAt;
    }

    public Long getQuotationId() {
        return quotationId;
    }

    public void setQuotationId(Long quotationId) {
        this.quotationId = quotationId;
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

    public List<QuotationItem> getQuotationItems() {
        return quotationItems;
    }

    public void setQuotationItems(List<QuotationItem> quotationItems) {
        this.quotationItems = quotationItems;
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

    public QuotationType getQuotationType() {
        return quotationType;
    }

    public void setQuotationType(QuotationType quotationType) {
        this.quotationType = quotationType;
    }
}
