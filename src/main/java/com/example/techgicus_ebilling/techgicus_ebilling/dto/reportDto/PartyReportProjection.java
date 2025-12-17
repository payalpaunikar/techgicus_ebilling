package com.example.techgicus_ebilling.techgicus_ebilling.dto.reportDto;

import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.GstType;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.State;

public interface PartyReportProjection {
    Long getPartyId();
    String getPartyName();
    String getGstin();
    String getPhoneNo();
    GstType getGstType();
    State getState();
    String getEmailId();
    String getbillingAddress();
    String getShipingAddress();
    Double getTotalDebit();
    Double getTotalCredit();
}
