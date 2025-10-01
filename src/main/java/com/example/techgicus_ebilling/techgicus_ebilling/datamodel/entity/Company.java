package com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity;

import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.BussinessType;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.State;
import jakarta.persistence.*;

import java.time.LocalDateTime;


@Entity
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long companyId;

    private String bussinessName;

    private String businessDescription;

    private String phoneNo;

    private String emailId;

    private String logoUrl;

    private String address;

    @Enumerated(EnumType.STRING)
    private BussinessType bussinessType;

    private String gstin;

    private String bussinessCategory;

    @Enumerated(EnumType.STRING)
    private State state;

    private String signatureUrl;

    @ManyToOne
    @JoinColumn(name = "company_owner_id",nullable = false)
    private User companyOwner;

    private LocalDateTime createdAt;

    private LocalDateTime updateAt;

    public Company() {
    }

    public Company(Long companyId, String bussinessName, String businessDescription, String phoneNo, String emailId, String logoUrl, String address, BussinessType bussinessType, String gstin, String bussinessCategory, State state, String signatureUrl, User companyOwner, LocalDateTime createdAt, LocalDateTime updateAt) {
        this.companyId = companyId;
        this.bussinessName = bussinessName;
        this.businessDescription = businessDescription;
        this.phoneNo = phoneNo;
        this.emailId = emailId;
        this.logoUrl = logoUrl;
        this.address = address;
        this.bussinessType = bussinessType;
        this.gstin = gstin;
        this.bussinessCategory = bussinessCategory;
        this.state = state;
        this.signatureUrl = signatureUrl;
        this.companyOwner = companyOwner;
        this.createdAt = createdAt;
        this.updateAt = updateAt;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public String getBussinessName() {
        return bussinessName;
    }

    public void setBussinessName(String bussinessName) {
        this.bussinessName = bussinessName;
    }

    public String getBusinessDescription() {
        return businessDescription;
    }

    public void setBusinessDescription(String businessDescription) {
        this.businessDescription = businessDescription;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public BussinessType getBussinessType() {
        return bussinessType;
    }

    public void setBussinessType(BussinessType bussinessType) {
        this.bussinessType = bussinessType;
    }

    public String getGstin() {
        return gstin;
    }

    public void setGstin(String gstin) {
        this.gstin = gstin;
    }

    public String getBussinessCategory() {
        return bussinessCategory;
    }

    public void setBussinessCategory(String bussinessCategory) {
        this.bussinessCategory = bussinessCategory;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public String getSignatureUrl() {
        return signatureUrl;
    }

    public void setSignatureUrl(String signatureUrl) {
        this.signatureUrl = signatureUrl;
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

    public User getCompanyOwner() {
        return companyOwner;
    }

    public void setCompanyOwner(User companyOwner) {
        this.companyOwner = companyOwner;
    }
}
