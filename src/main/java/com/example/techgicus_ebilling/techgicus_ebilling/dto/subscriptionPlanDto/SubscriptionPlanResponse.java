package com.example.techgicus_ebilling.techgicus_ebilling.dto.subscriptionPlanDto;


import java.math.BigDecimal;

public class SubscriptionPlanResponse {
    private Long subscriptionPlanId;
    private String name;
    private BigDecimal price;
    private Integer durationDays;
    private String description;
    private boolean isActive = true;

    public SubscriptionPlanResponse() {
    }

    public SubscriptionPlanResponse(Long subscriptionPlanId, String name, BigDecimal price, Integer durationDays, String description, boolean isActive) {
        this.subscriptionPlanId = subscriptionPlanId;
        this.name = name;
        this.price = price;
        this.durationDays = durationDays;
        this.description = description;
        this.isActive = isActive;
    }

    public Long getSubscriptionPlanId() {
        return subscriptionPlanId;
    }

    public void setSubscriptionPlanId(Long subscriptionPlanId) {
        this.subscriptionPlanId = subscriptionPlanId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getDurationDays() {
        return durationDays;
    }

    public void setDurationDays(Integer durationDays) {
        this.durationDays = durationDays;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
