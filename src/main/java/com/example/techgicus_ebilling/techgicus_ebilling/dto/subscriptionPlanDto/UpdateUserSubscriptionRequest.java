package com.example.techgicus_ebilling.techgicus_ebilling.dto.subscriptionPlanDto;

public class UpdateUserSubscriptionRequest {
    private Long userCurrentSubscriptionPlanId;
    private Long userUpdateSubscriptionPlanId;

    public UpdateUserSubscriptionRequest() {
    }

    public UpdateUserSubscriptionRequest(Long userCurrentSubscriptionPlanId, Long userUpdateSubscriptionPlanId) {
        this.userCurrentSubscriptionPlanId = userCurrentSubscriptionPlanId;
        this.userUpdateSubscriptionPlanId = userUpdateSubscriptionPlanId;
    }

    public Long getUserCurrentSubscriptionPlanId() {
        return userCurrentSubscriptionPlanId;
    }

    public void setUserCurrentSubscriptionPlanId(Long userCurrentSubscriptionPlanId) {
        this.userCurrentSubscriptionPlanId = userCurrentSubscriptionPlanId;
    }

    public Long getUserUpdateSubscriptionPlanId() {
        return userUpdateSubscriptionPlanId;
    }

    public void setUserUpdateSubscriptionPlanId(Long userUpdateSubscriptionPlanId) {
        this.userUpdateSubscriptionPlanId = userUpdateSubscriptionPlanId;
    }
}
