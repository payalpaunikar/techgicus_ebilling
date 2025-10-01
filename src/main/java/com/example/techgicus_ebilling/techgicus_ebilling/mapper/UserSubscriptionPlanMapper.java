package com.example.techgicus_ebilling.techgicus_ebilling.mapper;


import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.UserSubscription;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.subscriptionPlanDto.BuySubscriptionPlanResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserSubscriptionPlanMapper {

    @Mapping(target = "userSubscriptionId",source = "userSubscriptionId")
    @Mapping(target = "userId",source = "user.userId")
    @Mapping(target = "subscriptionId",source = "subscriptionPlan.subscriptionPlanId")
    @Mapping(target = "startDateTime",source = "startDate")
    @Mapping(target = "endDateTime",source = "endDate")
    @Mapping(target = "priceAtPurchase",source = "priceAtPurchase")
    @Mapping(target = "durationAtPurchase",source = "durationAtPurchase")
    @Mapping(target = "nameAtPurchase",source = "nameAtPurchase")
    @Mapping(target = "active",source = "active")
    BuySubscriptionPlanResponse convertUserSubscriptionPlanIntoResponse(UserSubscription userSubscription);
}
