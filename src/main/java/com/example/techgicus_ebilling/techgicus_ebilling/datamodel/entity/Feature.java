package com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity;


import jakarta.persistence.*;


import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "features")
public class Feature {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long featureId;

    @Column(nullable = false, unique = true)
    private String code; // "MULTI_BRANCH", "GST_FILING"

    @Column(nullable = false)
    private String name;

    @Column(length = 500)
    private String description;

    @Column(nullable = false)
    private boolean premium = false;

//    @ManyToMany(mappedBy = "features")
//    private Set<SubscriptionPlan> plans = new HashSet<>();

    @OneToMany(mappedBy = "feature")
    private Set<PlanFeatureLimit> planFeatureLimits = new HashSet<>();

    public Feature() {
    }

//    public Feature(Long featureId, String code, String name, String description, boolean premium, Set<SubscriptionPlan> plans) {
//        this.featureId = featureId;
//        this.code = code;
//        this.name = name;
//        this.description = description;
//        this.premium = premium;
//        this.plans = plans;
//    }


    public Feature(Long featureId, String code, String name, String description, boolean premium, Set<PlanFeatureLimit> planFeatureLimits) {
        this.featureId = featureId;
        this.code = code;
        this.name = name;
        this.description = description;
        this.premium = premium;
        this.planFeatureLimits = planFeatureLimits;
    }

    public Long getFeatureId() {
        return featureId;
    }

    public void setFeatureId(Long featureId) {
        this.featureId = featureId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isPremium() {
        return premium;
    }

    public void setPremium(boolean premium) {
        this.premium = premium;
    }

//    public Set<SubscriptionPlan> getPlans() {
//        return plans;
//    }
//
//    public void setPlans(Set<SubscriptionPlan> plans) {
//        this.plans = plans;
//    }


    public Set<PlanFeatureLimit> getPlanFeatureLimits() {
        return planFeatureLimits;
    }

    public void setPlanFeatureLimits(Set<PlanFeatureLimit> planFeatureLimits) {
        this.planFeatureLimits = planFeatureLimits;
    }

    // Consider adding equals/hashCode based on code since it's unique
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Feature)) return false;
        return code != null && code.equals(((Feature) o).getCode());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
