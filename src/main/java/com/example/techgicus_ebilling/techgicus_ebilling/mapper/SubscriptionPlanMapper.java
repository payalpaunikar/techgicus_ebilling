package com.example.techgicus_ebilling.techgicus_ebilling.mapper;


import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.SubscriptionPlan;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.subscriptionPlanDto.SubscriptionPlanResponse;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.subscriptionPlanDto.SubscriptionPlanWithFeatures;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = { PlanFeatureLimitMapper.class })
public interface SubscriptionPlanMapper {

    // Map planFeatureLimits → featureResponseDtos
    @Mapping(source = "planFeatureLimits", target = "featureResponseDtos")
    SubscriptionPlanWithFeatures convertSubscriptionPlanIntoDto(SubscriptionPlan subscriptionPlan);

    List<SubscriptionPlanWithFeatures> convertSubscriptionPlansIntoDtos(List<SubscriptionPlan> subscriptionPlans);

    @Mapping(source = "durationDays",target = "durationDays")
    SubscriptionPlanResponse convertSubscriptionPlanIntoSubscriptionPlanResponse(SubscriptionPlan subscriptionPlan);
}
