package com.example.techgicus_ebilling.techgicus_ebilling.repository;


import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Feature;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.PlanFeatureLimit;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.SubscriptionPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlanFeatureLimitRepository extends JpaRepository<PlanFeatureLimit,Long> {

    Optional<PlanFeatureLimit> findBySubscriptionPlanAndFeature(SubscriptionPlan plan, Feature feature);
}
