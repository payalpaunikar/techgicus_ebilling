package com.example.techgicus_ebilling.techgicus_ebilling.dto.companyDto;


import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.BussinessType;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.State;

public class CompanyResponse {
    private Long companyId;
    private String bussinessName;
    private String businessDescription;
    private String phoneNo;
    private String emailId;
    private String address;
    private BussinessType bussinessType;
    private String gstin;
    private String bussinessCategory;
    private State state;
    private String logoUrl;
    private String signatureUrl;
    private String bankName;
    private String accountNo;
    private String ifscCode;
    private String accountHolderName;
    private String upiId;

    public CompanyResponse() {
    }

    public CompanyResponse(Long companyId, String bussinessName, String businessDescription, String phoneNo, String emailId, String address, BussinessType bussinessType, String gstin, String bussinessCategory, State state, String logoUrl, String signatureUrl) {
        this.companyId = companyId;
        this.bussinessName = bussinessName;
        this.businessDescription = businessDescription;
        this.phoneNo = phoneNo;
        this.emailId = emailId;
        this.address = address;
        this.bussinessType = bussinessType;
        this.gstin = gstin;
        this.bussinessCategory = bussinessCategory;
        this.state = state;
        this.logoUrl = logoUrl;
        this.signatureUrl = signatureUrl;
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



    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
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

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getSignatureUrl() {
        return signatureUrl;
    }

    public void setSignatureUrl(String signatureUrl) {
        this.signatureUrl = signatureUrl;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getIfscCode() {
        return ifscCode;
    }

    public void setIfscCode(String ifscCode) {
        this.ifscCode = ifscCode;
    }

    public String getAccountHolderName() {
        return accountHolderName;
    }

    public void setAccountHolderName(String accountHolderName) {
        this.accountHolderName = accountHolderName;
    }

    public String getUpiId() {
        return upiId;
    }

    public void setUpiId(String upiId) {
        this.upiId = upiId;
    }
}
