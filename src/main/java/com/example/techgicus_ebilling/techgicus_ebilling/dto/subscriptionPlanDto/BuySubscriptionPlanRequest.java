package com.example.techgicus_ebilling.techgicus_ebilling.dto.subscriptionPlanDto;


import io.swagger.v3.oas.annotations.media.Schema;

@Schema
public class BuySubscriptionPlanRequest {

    @Schema(example = "1")
    private Long userId;

    @Schema(example = "1")
    private Long subscriptionPlanId;

    public BuySubscriptionPlanRequest() {
    }

    public BuySubscriptionPlanRequest(Long userId, Long subscriptionPlanId) {
        this.userId = userId;
        this.subscriptionPlanId = subscriptionPlanId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getSubscriptionPlanId() {
        return subscriptionPlanId;
    }

    public void setSubscriptionPlanId(Long subscriptionPlanId) {
        this.subscriptionPlanId = subscriptionPlanId;
    }
}
