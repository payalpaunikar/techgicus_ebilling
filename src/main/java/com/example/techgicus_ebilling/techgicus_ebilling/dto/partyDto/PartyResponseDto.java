package com.example.techgicus_ebilling.techgicus_ebilling.dto.partyDto;

import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.GstType;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.State;

public class PartyResponseDto {

    private Long partyId;

    private String gstin;

    private String phoneNo;

    private GstType gstType;

    private State state;

    private String emailId;

    private String billingAddress;

    private String shipingAddress;

    public PartyResponseDto() {
    }

    public PartyResponseDto(Long partyId, String gstin, String phoneNo, GstType gstType, State state, String emailId, String billingAddress, String shipingAddress) {
        this.partyId = partyId;
        this.gstin = gstin;
        this.phoneNo = phoneNo;
        this.gstType = gstType;
        this.state = state;
        this.emailId = emailId;
        this.billingAddress = billingAddress;
        this.shipingAddress = shipingAddress;
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
}
