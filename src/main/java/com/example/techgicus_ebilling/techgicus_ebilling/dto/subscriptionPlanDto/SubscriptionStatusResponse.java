package com.example.techgicus_ebilling.techgicus_ebilling.dto.subscriptionPlanDto;

public class SubscriptionStatusResponse {
    private Boolean isActive;
    private Boolean isEXpired;
    private Boolean isCancelled;

    public SubscriptionStatusResponse() {
    }

    public SubscriptionStatusResponse(Boolean isActive, Boolean isEXpired, Boolean isCancelled) {
        this.isActive = isActive;
        this.isEXpired = isEXpired;
        this.isCancelled = isCancelled;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public Boolean getEXpired() {
        return isEXpired;
    }

    public void setEXpired(Boolean EXpired) {
        isEXpired = EXpired;
    }

    public Boolean getCancelled() {
        return isCancelled;
    }

    public void setCancelled(Boolean cancelled) {
        isCancelled = cancelled;
    }
}
