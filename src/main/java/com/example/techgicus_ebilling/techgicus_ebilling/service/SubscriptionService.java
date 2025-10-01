package com.example.techgicus_ebilling.techgicus_ebilling.service;


import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.SubscriptionPlan;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.User;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.UserSubscription;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.featureDto.FeatureResponseDto;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.subscriptionPlanDto.*;
import com.example.techgicus_ebilling.techgicus_ebilling.exception.ResourceNotFoundException;
import com.example.techgicus_ebilling.techgicus_ebilling.exception.UserSubscriptionNotActiveException;
import com.example.techgicus_ebilling.techgicus_ebilling.mapper.SubscriptionPlanMapper;
import com.example.techgicus_ebilling.techgicus_ebilling.mapper.UserSubscriptionPlanMapper;
import com.example.techgicus_ebilling.techgicus_ebilling.repository.SubscriptionPlanRepository;
import com.example.techgicus_ebilling.techgicus_ebilling.repository.UserRepository;
import com.example.techgicus_ebilling.techgicus_ebilling.repository.UserSubscriptionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SubscriptionService {

       private UserSubscriptionRepository userSubscriptionRepository;
       private UserRepository userRepository;
       private SubscriptionPlanRepository subscriptionPlanRepository;
       private SubscriptionPlanMapper subscriptionPlanMapper;
       private UserSubscriptionPlanMapper userSubscriptionPlanMapper;

     private static final Logger logger = LoggerFactory.getLogger(SubscriptionService.class);

    @Autowired
    public SubscriptionService(UserSubscriptionRepository userSubscriptionRepository, UserRepository userRepository,
                               SubscriptionPlanRepository subscriptionPlanRepository,
                               SubscriptionPlanMapper subscriptionPlanMapper,
                               UserSubscriptionPlanMapper userSubscriptionPlanMapper) {
        this.userSubscriptionRepository = userSubscriptionRepository;
        this.userRepository = userRepository;
        this.subscriptionPlanRepository = subscriptionPlanRepository;
        this.subscriptionPlanMapper = subscriptionPlanMapper;
        this.userSubscriptionPlanMapper = userSubscriptionPlanMapper;
    }


    public List<SubscriptionPlanWithFeatures> getSubscriptionPlansWithFeatures(User currentUser){

        List<SubscriptionPlan> subscriptionPlans = subscriptionPlanRepository.findAll();

        boolean isCurrentUserSuperAdmin = currentUser.getRoles()
                .stream()
                .anyMatch(role -> role.getRoleName().equals("SUPERADMIN"));

        if (isCurrentUserSuperAdmin){
         return subscriptionPlanMapper.convertSubscriptionPlansIntoDtos(subscriptionPlans);
        }

        return subscriptionPlanMapper.convertSubscriptionPlansIntoDtos( subscriptionPlans.stream()
                .filter(subscriptionPlan -> !"Lifetime".equals(subscriptionPlan.getName()))
                .toList());
    }


    public BuySubscriptionPlanResponse buySubscription(BuySubscriptionPlanRequest buySubscriptionPlanRequest){

        User user = userRepository.findById(buySubscriptionPlanRequest.getUserId())
                .orElseThrow(()-> new ResourceNotFoundException("User not found with id : "+buySubscriptionPlanRequest.getUserId()));

        SubscriptionPlan subscriptionPlan = subscriptionPlanRepository.findById(buySubscriptionPlanRequest.getSubscriptionPlanId())
                .orElseThrow(()-> new ResourceNotFoundException("Subscription plan not found with id : "+buySubscriptionPlanRequest.getSubscriptionPlanId()));

        UserSubscription userSubscription = new UserSubscription();
        userSubscription.setSubscriptionPlan(subscriptionPlan);
        userSubscription.setUser(user);
        userSubscription.setDurationAtPurchase(subscriptionPlan.getDurationDays());
        userSubscription.setNameAtPurchase(subscriptionPlan.getName());
        userSubscription.setPriceAtPurchase(subscriptionPlan.getPrice());
        userSubscription.setStartDate(LocalDateTime.now());
        userSubscription.setEndDate(LocalDateTime.now().plusDays(subscriptionPlan.getDurationDays()-1));
        userSubscription.setActive(true);
        userSubscription.setCreatedAt(LocalDateTime.now());
        userSubscription.setUpdatedAt(LocalDateTime.now());


        UserSubscription saveUserSubscription = userSubscriptionRepository.save(userSubscription);

       BuySubscriptionPlanResponse buySubscriptionPlanResponse = userSubscriptionPlanMapper.convertUserSubscriptionPlanIntoResponse(saveUserSubscription);

       return buySubscriptionPlanResponse;

    }


    public SubscriptionStatusResponse isUserSubscriptionActive(Long userId){

        User user = userRepository.findById(userId)
                .orElseThrow(()-> new ResourceNotFoundException("User not found with id : "+userId));

        Optional<UserSubscription> userSubscription = userSubscriptionRepository.findActiveSubscriptionByUserId(userId);

        Map<String,Boolean> isActive = new HashMap<>();

        SubscriptionStatusResponse subscriptionStatusResponse = new SubscriptionStatusResponse();

        if (userSubscription.isEmpty()){
            subscriptionStatusResponse.setActive(false);
            return subscriptionStatusResponse;
        }

        subscriptionStatusResponse.setActive(true);
        return subscriptionStatusResponse;
    }


    public BuySubscriptionPlanResponse userCurrentPlan(Long userId){

        User user = userRepository.findById(userId)
                .orElseThrow(()-> new ResourceNotFoundException("User not found with id : "+userId));

        Optional<UserSubscription> userSubscription = userSubscriptionRepository.findActiveSubscriptionByUserId(userId);

        if (userSubscription.isPresent()){
            BuySubscriptionPlanResponse subscriptionPlanResponse = userSubscriptionPlanMapper.convertUserSubscriptionPlanIntoResponse(userSubscription.get());

            return subscriptionPlanResponse;
        }

        throw new UserSubscriptionNotActiveException("User doesn't have any plan active");
    }


    public CurrentAndAvailablePlanResponse currentAndAvailablePlan(Long userId){

        User user = userRepository.findById(userId)
                .orElseThrow(()-> new ResourceNotFoundException("User not found with id : "+userId));

        Optional<UserSubscription> userSubscription = userSubscriptionRepository.findActiveSubscriptionByUserId(userId);

        List<SubscriptionPlan> subscriptionPlans = subscriptionPlanRepository.findAll();

        if (userSubscription.isPresent()){

          subscriptionPlans = subscriptionPlans.stream()
                    .filter(plan-> !plan.equals(userSubscription.get().getSubscriptionPlan()))
                    .toList();

            List<SubscriptionPlanWithFeatures> subscriptionPlanWithFeatures =  subscriptionPlanMapper.convertSubscriptionPlansIntoDtos(subscriptionPlans);

            SubscriptionPlanWithFeatures currentPlan = subscriptionPlanMapper.convertSubscriptionPlanIntoDto(userSubscription.get().getSubscriptionPlan());
            CurrentAndAvailablePlanResponse currentAndAvailablePlanResponse = new CurrentAndAvailablePlanResponse();
            currentAndAvailablePlanResponse.setAvailablePlanWithFeatures(subscriptionPlanWithFeatures);
            currentAndAvailablePlanResponse.setSubscriptionPlanWithFeatures(currentPlan);

            return currentAndAvailablePlanResponse;

        }
        else {
            throw new UserSubscriptionNotActiveException("User doesn't have any plan active");
        }

    }


    @Transactional
    public BuySubscriptionPlanResponse updateUserSubscription(Long userId,UpdateUserSubscriptionRequest updateUserSubscriptionRequest){

        User user = userRepository.findById(userId)
                .orElseThrow(()-> new ResourceNotFoundException("User not found with id : "+userId));

        UserSubscription userSubscription = userSubscriptionRepository.findActiveSubscriptionByUserId(userId)
                .orElseThrow(()-> new ResourceNotFoundException("User suscription is not active right now"));

        if (userSubscription.getUser().getUserId() == userId && userSubscription.getSubscriptionPlan().getSubscriptionPlanId()==updateUserSubscriptionRequest.getUserCurrentSubscriptionPlanId()){

            SubscriptionPlan subscriptionPlan = subscriptionPlanRepository.findById(updateUserSubscriptionRequest.getUserUpdateSubscriptionPlanId())
                    .orElseThrow(()-> new ResourceNotFoundException("Subscription Plan not found with id : "+updateUserSubscriptionRequest.getUserUpdateSubscriptionPlanId()));

            userSubscription.setActive(false);
            userSubscription.setCancelled(true);
            userSubscription.setUpdatedAt(LocalDateTime.now());

            userSubscriptionRepository.save(userSubscription);

            UserSubscription updateUserSubscription = new UserSubscription();
            updateUserSubscription.setSubscriptionPlan(subscriptionPlan);
            updateUserSubscription.setUser(user);
            updateUserSubscription.setNameAtPurchase(subscriptionPlan.getName());
            updateUserSubscription.setPriceAtPurchase(subscriptionPlan.getPrice());
            updateUserSubscription.setDurationAtPurchase(subscriptionPlan.getDurationDays());
            updateUserSubscription.setStartDate(LocalDateTime.now());
            updateUserSubscription.setEndDate(userSubscription.getEndDate());
            updateUserSubscription.setCreatedAt(LocalDateTime.now());
            updateUserSubscription.setUpdatedAt(LocalDateTime.now());
            updateUserSubscription.setActive(true);

            UserSubscription saveUserSubscription =  userSubscriptionRepository.save(updateUserSubscription);

             BuySubscriptionPlanResponse buySubscriptionPlanResponse = userSubscriptionPlanMapper.convertUserSubscriptionPlanIntoResponse(saveUserSubscription);

             return buySubscriptionPlanResponse;
        }

        throw new RuntimeException("User Subscription not match with current plan");

    }



    public SubscriptionPlanResponse getSubscriptionById(Long subscriptionPlanId){
        SubscriptionPlan subscriptionPlan = subscriptionPlanRepository.findById(subscriptionPlanId)
                .orElseThrow(()-> new ResourceNotFoundException("Subscription plan not found with id : "+subscriptionPlanId));


        SubscriptionPlanResponse subscriptionPlanResponse = subscriptionPlanMapper.convertSubscriptionPlanIntoSubscriptionPlanResponse(subscriptionPlan);

        return subscriptionPlanResponse;
    }




}
