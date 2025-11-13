package com.example.techgicus_ebilling.techgicus_ebilling.dto.subscriptionPlanDto;

public class InitiateResponse {
    private String orderId;
    private Integer amount;
    private String currency;
    private String key;
    private Long localSubscriptionId;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Long getLocalSubscriptionId() {
        return localSubscriptionId;
    }

    public void setLocalSubscriptionId(Long localSubscriptionId) {
        this.localSubscriptionId = localSubscriptionId;
    }
}
