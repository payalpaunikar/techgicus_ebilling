package com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity;

import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.PaymentType;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.State;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Entity
public class SaleReturn {

      @Id
      @GeneratedValue(strategy = GenerationType.IDENTITY)
      private Long saleReturnId;

      private String returnNo;

      private LocalDate returnDate;

      private String phoneNo;

      private LocalDate invoiceDate;

      private String invoiceNo;

      private Double totalDiscount;

      private Double totalTaxAmount;

      private Integer totalQuantity;

      private Double totalAmount;

      private Double paidAmount;

      private Double balanceAmount;

      @Enumerated(EnumType.STRING)
      private PaymentType paymentType;

      @Enumerated(EnumType.STRING)
      private State stateOfSupply;

      private String description;

      @ManyToOne
      @JoinColumn(name = "party_id")
      private Party party;

      @ManyToOne
      @JoinColumn(name = "company_id")
      private Company company;

      @OneToMany(mappedBy = "saleReturn",cascade = CascadeType.ALL,orphanRemoval = true)
      private List<SaleReturnItem> saleReturnItems = new ArrayList<>();


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

    public Party getParty() {
        return party;
    }

    public void setParty(Party party) {
        this.party = party;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public List<SaleReturnItem> getSaleReturnItems() {
        return saleReturnItems;
    }

    public void setSaleReturnItems(List<SaleReturnItem> saleReturnItems) {
        this.saleReturnItems = saleReturnItems;
    }
}
