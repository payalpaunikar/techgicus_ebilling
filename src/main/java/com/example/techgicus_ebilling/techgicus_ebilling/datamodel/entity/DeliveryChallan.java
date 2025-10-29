package com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity;

import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.ChallanType;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.State;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Entity
public class DeliveryChallan {

      @Id
      @GeneratedValue(strategy = GenerationType.IDENTITY)
      private Long deliveryChallanId;

      private String challanNo;

      private LocalDate challanDate;

      private LocalDate dueDate;

      private Double totalDiscountAmount;

      private Double totalTaxAmount;

      private Double totalQuantity;

      private Double totalAmount;

      @Enumerated(EnumType.STRING)
      private State stateOfSupply;

      private String description;

      @Enumerated(EnumType.STRING)
      private ChallanType challanType;

      @ManyToOne
      @JoinColumn(name = "party_id")
      private Party party;

      @ManyToOne
      @JoinColumn(name = "company_id")
      private Company company;


      @OneToMany(mappedBy = "deliveryChallan",cascade = CascadeType.ALL,orphanRemoval = true)
      private List<DeliveryChallanItem> deliveryChallanItems = new ArrayList<>();

    public Long getDeliveryChallanId() {
        return deliveryChallanId;
    }

    public void setDeliveryChallanId(Long deliveryChallanId) {
        this.deliveryChallanId = deliveryChallanId;
    }

    public String getChallanNo() {
        return challanNo;
    }

    public void setChallanNo(String challanNo) {
        this.challanNo = challanNo;
    }

    public LocalDate getChallanDate() {
        return challanDate;
    }

    public void setChallanDate(LocalDate challanDate) {
        this.challanDate = challanDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public Double getTotalDiscountAmount() {
        return totalDiscountAmount;
    }

    public void setTotalDiscountAmount(Double totalDiscountAmount) {
        this.totalDiscountAmount = totalDiscountAmount;
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

    public List<DeliveryChallanItem> getDeliveryChallanItems() {
        return deliveryChallanItems;
    }

    public void setDeliveryChallanItems(List<DeliveryChallanItem> deliveryChallanItems) {
        this.deliveryChallanItems = deliveryChallanItems;
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

    public ChallanType getChallanType() {
        return challanType;
    }

    public void setChallanType(ChallanType challanType) {
        this.challanType = challanType;
    }
}
