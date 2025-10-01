package com.example.techgicus_ebilling.techgicus_ebilling.repository;

import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.SubscriptionPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface SubscriptionPlanRepository extends JpaRepository<SubscriptionPlan,Long> {

    Optional<SubscriptionPlan> findByName(String subscriptionName);
}
