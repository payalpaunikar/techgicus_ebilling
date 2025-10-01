package com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity;


import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.LimitType;
import jakarta.persistence.*;

@Entity
@Table(name = "plan_feature_limits",
       uniqueConstraints = {
        @UniqueConstraint(columnNames = {"plan_id","feature_id"})
       })
public class PlanFeatureLimit {

     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     private Long planFeatureLimitId;


     @ManyToOne
     @JoinColumn(name = "plan_id",nullable = false)
     private SubscriptionPlan subscriptionPlan;


     @ManyToOne
     @JoinColumn(name = "feature_id",nullable = false)
     private Feature feature;

    @Enumerated(EnumType.STRING)
    private LimitType limitType; // ENUM: BOOLEAN or INTEGER

    private Integer usageLimit; // used only if limitType is INTEGER

    private Boolean booleanLimit; // used only if limitType is BOOLEAN

    // ✅ JPA Lifecycle Hook: called automatically before insert/update the entity
    // what happen when we add boolean and limit type and we deoesn't add the boolean limit
    // it throw exception... and vise versa for the limit type integer.
//    @PrePersist
//    @PreUpdate
//    private void validateLimit() {
//        if (limitType == LimitType.BOOLEAN && booleanLimit == null) {
//            throw new IllegalStateException("Boolean limit required for BOOLEAN type.");
//        }
//        if (limitType == LimitType.INTEGER && usageLimit == null) {
//            throw new IllegalStateException("Usage limit required for INTEGER type.");
//        }
//    }


    // Consider adding a business method to get the limit value
    public Object getLimitValue() {
        return limitType == LimitType.BOOLEAN ? booleanLimit : usageLimit;
    }


    public PlanFeatureLimit() {
    }

    public PlanFeatureLimit(Long planFeatureLimitId, SubscriptionPlan subscriptionPlan, Feature feature, LimitType limitType, Integer usageLimit, Boolean booleanLimit) {
        this.planFeatureLimitId = planFeatureLimitId;
        this.subscriptionPlan = subscriptionPlan;
        this.feature = feature;
        this.limitType = limitType;
        this.usageLimit = usageLimit;
        this.booleanLimit = booleanLimit;
    }

    public Long getPlanFeatureLimitId() {
        return planFeatureLimitId;
    }

    public void setPlanFeatureLimitId(Long planFeatureLimitId) {
        this.planFeatureLimitId = planFeatureLimitId;
    }

    public SubscriptionPlan getSubscriptionPlan() {
        return subscriptionPlan;
    }

    public void setSubscriptionPlan(SubscriptionPlan subscriptionPlan) {
        this.subscriptionPlan = subscriptionPlan;
    }

    public Feature getFeature() {
        return feature;
    }

    public void setFeature(Feature feature) {
        this.feature = feature;
    }

    public LimitType getLimitType() {
        return limitType;
    }

    public void setLimitType(LimitType limitType) {
        this.limitType = limitType;
    }

    public Integer getUsageLimit() {
        return usageLimit;
    }

    public void setUsageLimit(Integer usageLimit) {
        this.usageLimit = usageLimit;
    }

    public Boolean getBooleanLimit() {
        return booleanLimit;
    }

    public void setBooleanLimit(Boolean booleanLimit) {
        this.booleanLimit = booleanLimit;
    }
}
