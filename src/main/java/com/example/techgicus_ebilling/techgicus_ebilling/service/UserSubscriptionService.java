package com.example.techgicus_ebilling.techgicus_ebilling.service;


import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Feature;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.PlanFeatureLimit;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.SubscriptionPlan;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.UserSubscription;
import com.example.techgicus_ebilling.techgicus_ebilling.exception.ResourceNotFoundException;
import com.example.techgicus_ebilling.techgicus_ebilling.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserSubscriptionService {

         private final UserSubscriptionRepository userSubscriptionRepository;
         private final CompanyRepository companyRepository;
         private final UserRepository userRepository;
         private final PlanFeatureLimitRepository planFeatureLimitRepository;
         private final FeatureRepository featureRepository;

         private final static Logger logger = LoggerFactory.getLogger(UserSubscriptionService.class);

    @Autowired
    public UserSubscriptionService(UserSubscriptionRepository userSubscriptionRepository, CompanyRepository companyRepository, UserRepository userRepository, PlanFeatureLimitRepository planFeatureLimitRepository, FeatureRepository featureRepository) {
        this.userSubscriptionRepository = userSubscriptionRepository;
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
        this.planFeatureLimitRepository = planFeatureLimitRepository;
        this.featureRepository = featureRepository;
    }

    public boolean isUserSubscriptionActive(Long userId){
//        User user = userRepository.findById(userId)
//                .orElseThrow(()-> new ResourceNotFoundException("User not found with id : "+userId));

        Optional<UserSubscription> userSubscription = userSubscriptionRepository.findActiveSubscriptionByUserId(userId);

        if (userSubscription.isEmpty()){
            return false;
        }
        return true;
    }


    public boolean canUserCreateCompany(Long userId){
        Optional<UserSubscription> userSubscription = userSubscriptionRepository.findActiveSubscriptionByUserId(userId);

        if (userSubscription.isEmpty()) return false;

        String multipleCompanyCreatedCode = "CREATE_MULTI_COMPANY";

        Feature feature = featureRepository.findByCode(multipleCompanyCreatedCode)
                .orElseThrow(()-> new ResourceNotFoundException("Feature not found with code : "+multipleCompanyCreatedCode));

        SubscriptionPlan userSubscriptionPlan = userSubscription.get().getSubscriptionPlan();

        PlanFeatureLimit planFeatureLimit = planFeatureLimitRepository.findBySubscriptionPlanAndFeature(userSubscriptionPlan,feature)
                .orElseThrow(()-> new ResourceNotFoundException("Subscription plan with feature limit is not set"));

        long totalComapanyCreated = companyRepository.countByCompanyOwner_UserId(userId);

        if (planFeatureLimit.getUsageLimit() > totalComapanyCreated){
            logger.info("true");
            return true;
        }

        logger.info("false");
        return false;

        
    }
}
