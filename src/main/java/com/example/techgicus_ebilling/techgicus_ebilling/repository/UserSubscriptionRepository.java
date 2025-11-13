package com.example.techgicus_ebilling.techgicus_ebilling.repository;


import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.UserSubscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserSubscriptionRepository extends JpaRepository<UserSubscription,Long> {

    @Query("SELECT us FROM UserSubscription us WHERE us.user.userId =:userId AND us.isActive=true ")
    Optional<UserSubscription> findActiveSubscriptionByUserId(@Param("userId")Long userId);

    UserSubscription findByRazorpayOrderId(String orderId);


}
