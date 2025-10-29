package com.example.techgicus_ebilling.techgicus_ebilling.dto.deliveryChallanDto;

import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.ChallanType;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.State;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.partyDto.PartyResponseDto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DeliveryChallanResponse {

    private Long deliveryChallanId;

    private String challanNo;

    private LocalDate challanDate;

    private LocalDate dueDate;

    private Double totalDiscountAmount;

    private Double totalTaxAmount;

    private Double totalQuantity;

    private Double totalAmount;

    private State stateOfSupply;

    private String description;

    private ChallanType challanType;

    private PartyResponseDto partyResponseDto;

    private List<DeliveryChallanItemResponse> deliveryChallanItemResponses = new ArrayList<>();

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

    public PartyResponseDto getPartyResponseDto() {
        return partyResponseDto;
    }

    public void setPartyResponseDto(PartyResponseDto partyResponseDto) {
        this.partyResponseDto = partyResponseDto;
    }

    public List<DeliveryChallanItemResponse> getDeliveryChallanItemResponses() {
        return deliveryChallanItemResponses;
    }

    public void setDeliveryChallanItemResponses(List<DeliveryChallanItemResponse> deliveryChallanItemResponses) {
        this.deliveryChallanItemResponses = deliveryChallanItemResponses;
    }

    public ChallanType getChallanType() {
        return challanType;
    }

    public void setChallanType(ChallanType challanType) {
        this.challanType = challanType;
    }
}
