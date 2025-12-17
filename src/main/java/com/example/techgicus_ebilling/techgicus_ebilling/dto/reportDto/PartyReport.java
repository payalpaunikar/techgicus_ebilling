package com.example.techgicus_ebilling.techgicus_ebilling.dto.reportDto;

import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.GstType;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.State;

public class PartyReport {
    private Long partyId;

    private String name;

    private String gstin;

    private String phoneNo;

    private GstType gstType;

    private State state;

    private String emailId;

    private String billingAddress;

    private String shipingAddress;
    private Double receivableBalance;
    private Double payableBalance;

    public Long getPartyId() {
        return partyId;
    }

    public void setPartyId(Long partyId) {
        this.partyId = partyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Double getReceivableBalance() {
        return receivableBalance;
    }

    public void setReceivableBalance(Double receivableBalance) {
        this.receivableBalance = receivableBalance;
    }

    public Double getPayableBalance() {
        return payableBalance;
    }

    public void setPayableBalance(Double payableBalance) {
        this.payableBalance = payableBalance;
    }
}
