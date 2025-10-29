package com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity;

import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.GstType;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.State;
import jakarta.persistence.*;

import java.time.LocalDateTime;


@Entity
public class Party {

     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     private Long partyId;

     private String name;

     private String gstin;

     private String phoneNo;

     @Enumerated(EnumType.STRING)
     private GstType gstType;

     @Enumerated(EnumType.STRING)
     private State state;

     private String emailId;

     private String billingAddress;

     private String shipingAddress;

     private LocalDateTime createdAt;

     private LocalDateTime updatedAt;

     @ManyToOne
     @JoinColumn(name = "company_id")
     private Company company;

    public Party() {
    }

    public Party(Long partyId, String name, String gstin, String phoneNo, GstType gstType, State state, String emailId, String billingAddress, String shipingAddress, LocalDateTime createdAt, LocalDateTime updatedAt, Company company) {
        this.partyId = partyId;
        this.name = name;
        this.gstin = gstin;
        this.phoneNo = phoneNo;
        this.gstType = gstType;
        this.state = state;
        this.emailId = emailId;
        this.billingAddress = billingAddress;
        this.shipingAddress = shipingAddress;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.company = company;
    }

    public Long getPartyId() {
        return partyId;
    }

    public void setPartyId(Long partyId) {
        this.partyId = partyId;
    }

    public String getGstin() {
        return gstin;
    }

    public void setGstin(String gstin) {
        this.gstin = gstin;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public GstType getGstType() {
        return gstType;
    }

    public void setGstType(GstType gstType) {
        this.gstType = gstType;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(String billingAddress) {
        this.billingAddress = billingAddress;
    }

    public String getShipingAddress() {
        return shipingAddress;
    }

    public void setShipingAddress(String shipingAddress) {
        this.shipingAddress = shipingAddress;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
