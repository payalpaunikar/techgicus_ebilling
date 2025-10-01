package com.example.techgicus_ebilling.techgicus_ebilling.dto.subscriptionPlanDto;

import java.util.List;

public class CurrentAndAvailablePlanResponse {
    private SubscriptionPlanWithFeatures subscriptionPlanWithFeatures;
    private List<SubscriptionPlanWithFeatures> availablePlanWithFeatures;

    public CurrentAndAvailablePlanResponse() {
    }

    public CurrentAndAvailablePlanResponse(SubscriptionPlanWithFeatures subscriptionPlanWithFeatures, List<SubscriptionPlanWithFeatures> availablePlanWithFeatures) {
        this.subscriptionPlanWithFeatures = subscriptionPlanWithFeatures;
        this.availablePlanWithFeatures = availablePlanWithFeatures;
    }

    public SubscriptionPlanWithFeatures getSubscriptionPlanWithFeatures() {
        return subscriptionPlanWithFeatures;
    }

    public void setSubscriptionPlanWithFeatures(SubscriptionPlanWithFeatures subscriptionPlanWithFeatures) {
        this.subscriptionPlanWithFeatures = subscriptionPlanWithFeatures;
    }

    public List<SubscriptionPlanWithFeatures> getAvailablePlanWithFeatures() {
        return availablePlanWithFeatures;
    }

    public void setAvailablePlanWithFeatures(List<SubscriptionPlanWithFeatures> availablePlanWithFeatures) {
        this.availablePlanWithFeatures = availablePlanWithFeatures;
    }
}
