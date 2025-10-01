package com.example.techgicus_ebilling.techgicus_ebilling.dto.subscriptionPlanDto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema
public class BuySubscriptionPlanResponse {

    @Schema(example = "1")
    private Long userSubscriptionId;

    @Schema(example = "1")
    private Long userId;

    @Schema(example = "1")
    private Long subscriptionId;

    @Schema(example = "2025-08-29T15:55:47.439190800")
    private LocalDateTime startDateTime;

    @Schema(example = "2026-08-29T15:55:47.439190800")
    private LocalDateTime endDateTime;

    @Schema(example = "399.0")
    private Double priceAtPurchase;

    @Schema(example = "365")
    private Integer durationAtPurchase;

    @Schema(example = "silver")
    private String nameAtPurchase;

    @Schema(example = "true")
    private Boolean isActive;

    public BuySubscriptionPlanResponse() {
    }

    public BuySubscriptionPlanResponse(Long userSubscriptionId,Long userId, Long subscriptionId, LocalDateTime startDateTime, LocalDateTime endDateTime, Double priceAtPurchase, Integer durationAtPurchase, String nameAtPurchase, Boolean isActive) {
        this.userSubscriptionId = userSubscriptionId;
        this.userId = userId;
        this.subscriptionId = subscriptionId;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.priceAtPurchase = priceAtPurchase;
        this.durationAtPurchase = durationAtPurchase;
        this.nameAtPurchase = nameAtPurchase;
        this.isActive = isActive;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(Long subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }

    public Double getPriceAtPurchase() {
        return priceAtPurchase;
    }

    public void setPriceAtPurchase(Double priceAtPurchase) {
        this.priceAtPurchase = priceAtPurchase;
    }

    public Integer getDurationAtPurchase() {
        return durationAtPurchase;
    }

    public void setDurationAtPurchase(Integer durationAtPurchase) {
        this.durationAtPurchase = durationAtPurchase;
    }

    public String getNameAtPurchase() {
        return nameAtPurchase;
    }

    public void setNameAtPurchase(String nameAtPurchase) {
        this.nameAtPurchase = nameAtPurchase;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public Long getUserSubscriptionId() {
        return userSubscriptionId;
    }

    public void setUserSubscriptionId(Long userSubscriptionId) {
        this.userSubscriptionId = userSubscriptionId;
    }
}
