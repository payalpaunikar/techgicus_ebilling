package com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity;


import jakarta.persistence.*;


import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "subscription_plans")
public class SubscriptionPlan {

     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     private Long subscriptionPlanId;

     @Column(nullable = false,unique = true)
     private String name; // Silver , Gold , lifetime , etc

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = true)
    private Integer durationDays; // null for lifetime

    @Column(length = 500)
    private String description;

    @Column(nullable = false)
    private boolean isActive = true;

//    @ManyToMany
//    @JoinTable(
//            name = "plan_features",
//            joinColumns = @JoinColumn(name = "plan_id"),
//            inverseJoinColumns = @JoinColumn(name = "feature_id")
//    )
//    private Set<Feature> features = new HashSet<>();


    @OneToMany(mappedBy = "subscriptionPlan",cascade = CascadeType.ALL,orphanRemoval = true)
    public Set<PlanFeatureLimit> planFeatureLimits = new HashSet<>();

    public SubscriptionPlan() {
    }

//    public SubscriptionPlan(Long subscriptionPlanId, String name, BigDecimal price, Integer durationDays, String description, boolean isActive, Set<Feature> features) {
//        this.subscriptionPlanId = subscriptionPlanId;
//        this.name = name;
//        this.price = price;
//        this.durationDays = durationDays;
//        this.description = description;
//        this.isActive = isActive;
//        this.features = features;
//    }


    public SubscriptionPlan(Long subscriptionPlanId, String name, BigDecimal price, Integer durationDays, String description, boolean isActive, Set<PlanFeatureLimit> planFeatureLimits) {
        this.subscriptionPlanId = subscriptionPlanId;
        this.name = name;
        this.price = price;
        this.durationDays = durationDays;
        this.description = description;
        this.isActive = isActive;
        this.planFeatureLimits = planFeatureLimits;
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

//    public Set<Feature> getFeatures() {
//        return features;
//    }
//
//    public void setFeatures(Set<Feature> features) {
//        this.features = features;
//    }
//
//
//    // helper methods
//    public void addFeatures(Feature feature){
//        this.features.add(feature);
//        feature.getPlans().add(this);
//    }
//
//    public void removeFeature(Feature feature){
//        this.features.remove(feature);
//        feature.getPlans().remove(this);
//    }


    public Set<PlanFeatureLimit> getPlanFeatureLimits() {
        return planFeatureLimits;
    }

    public void setPlanFeatureLimits(Set<PlanFeatureLimit> planFeatureLimits) {
        this.planFeatureLimits = planFeatureLimits;
    }
}
