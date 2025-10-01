package com.example.techgicus_ebilling.techgicus_ebilling.dto.subscriptionPlanDto;

import com.example.techgicus_ebilling.techgicus_ebilling.dto.featureDto.FeatureResponseDto;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.Set;


@Schema(description = "Gives details about Subscription Plan with specific feature list of the plan")
public class SubscriptionPlanWithFeatures {

    @Schema(example = "1",type = "number",description = "Unique ID of the subscription")
    private Long subscriptionPlanId;

    @Schema(example = "Silver",type = "String",description = "Name of the subscription plan")
    private String name;

    @Schema(example = "399.0",type = "number",description = "Price of the subscription plan")
    private BigDecimal price;

    @Schema(example = "365",type = "number")
    private Integer durationDays;

    @Schema(example = "This is silver plan",type = "String")
    private String description;

    //@Schema(description = "List of features included in the subscription",type = "array")
    @ArraySchema(
            schema = @Schema(implementation = FeatureResponseDto.class)
            //arraySchema = @Schema(description = "List of features included in the subscription")
    )
    private Set<FeatureResponseDto> featureResponseDtos;

    public SubscriptionPlanWithFeatures() {
    }

    public SubscriptionPlanWithFeatures(Long subscriptionPlanId, String name, BigDecimal price, Integer durationDays, String description, Set<FeatureResponseDto> featureResponseDtos) {
        this.subscriptionPlanId = subscriptionPlanId;
        this.name = name;
        this.price = price;
        this.durationDays = durationDays;
        this.description = description;
        this.featureResponseDtos = featureResponseDtos;
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

    public Set<FeatureResponseDto> getFeatureResponseDtos() {
        return featureResponseDtos;
    }

    public void setFeatureResponseDtos(Set<FeatureResponseDto> featureResponseDtos) {
        this.featureResponseDtos = featureResponseDtos;
    }
}
