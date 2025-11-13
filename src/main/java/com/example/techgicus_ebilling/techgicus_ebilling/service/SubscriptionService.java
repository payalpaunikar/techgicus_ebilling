package com.example.techgicus_ebilling.techgicus_ebilling.service;


import com.example.techgicus_ebilling.techgicus_ebilling.config.RazorpayConfig;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.SubscriptionPlan;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.User;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.UserSubscription;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.SubscriptionStatus;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.subscriptionPlanDto.*;
import com.example.techgicus_ebilling.techgicus_ebilling.exception.PaymentProcessingException;
import com.example.techgicus_ebilling.techgicus_ebilling.exception.ResourceNotFoundException;
import com.example.techgicus_ebilling.techgicus_ebilling.exception.UserSubscriptionNotActiveException;
import com.example.techgicus_ebilling.techgicus_ebilling.mapper.SubscriptionPlanMapper;
import com.example.techgicus_ebilling.techgicus_ebilling.mapper.UserSubscriptionPlanMapper;
import com.example.techgicus_ebilling.techgicus_ebilling.repository.SubscriptionPlanRepository;
import com.example.techgicus_ebilling.techgicus_ebilling.repository.UserRepository;
import com.example.techgicus_ebilling.techgicus_ebilling.repository.UserSubscriptionRepository;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.apache.commons.codec.digest.HmacUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class SubscriptionService {

       private UserSubscriptionRepository userSubscriptionRepository;
       private UserRepository userRepository;
       private SubscriptionPlanRepository subscriptionPlanRepository;
       private SubscriptionPlanMapper subscriptionPlanMapper;
       private UserSubscriptionPlanMapper userSubscriptionPlanMapper;
       private RazorpayConfig razorpayConfig;
       private RazorpayClient razorpayClient;


     private static final Logger logger = LoggerFactory.getLogger(SubscriptionService.class);

    @Autowired
    public SubscriptionService(UserSubscriptionRepository userSubscriptionRepository, UserRepository userRepository, SubscriptionPlanRepository subscriptionPlanRepository, SubscriptionPlanMapper subscriptionPlanMapper, UserSubscriptionPlanMapper userSubscriptionPlanMapper, RazorpayConfig razorpayConfig, RazorpayClient razorpayClient) {
        this.userSubscriptionRepository = userSubscriptionRepository;
        this.userRepository = userRepository;
        this.subscriptionPlanRepository = subscriptionPlanRepository;
        this.subscriptionPlanMapper = subscriptionPlanMapper;
        this.userSubscriptionPlanMapper = userSubscriptionPlanMapper;
        this.razorpayConfig = razorpayConfig;
        this.razorpayClient = razorpayClient;
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


    @Transactional
    public InitiateResponse initiateBuySubscription(BuySubscriptionPlanRequest buySubscriptionPlanRequest){

        User user = userRepository.findById(buySubscriptionPlanRequest.getUserId())
                .orElseThrow(()-> new ResourceNotFoundException("User not found with id : "+buySubscriptionPlanRequest.getUserId()));

        SubscriptionPlan subscriptionPlan = subscriptionPlanRepository.findById(buySubscriptionPlanRequest.getSubscriptionPlanId())
                .orElseThrow(()-> new ResourceNotFoundException("Subscription plan not found with id : "+buySubscriptionPlanRequest.getSubscriptionPlanId()));

        // compute paise
        int amountInPaise = subscriptionPlan.getPrice().multiply(BigDecimal.valueOf(100)).intValueExact();

        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", amountInPaise);
        orderRequest.put("currency", "INR");
        orderRequest.put("receipt", "sub_rcpt_" + System.currentTimeMillis());
        orderRequest.put("payment_capture", 1);


        try {
            Order order = razorpayClient.orders.create(orderRequest);


        UserSubscription userSubscription = new UserSubscription();
        userSubscription.setSubscriptionPlan(subscriptionPlan);
        userSubscription.setUser(user);
        userSubscription.setDurationAtPurchase(subscriptionPlan.getDurationDays());
        userSubscription.setNameAtPurchase(subscriptionPlan.getName());
        userSubscription.setPriceAtPurchase(subscriptionPlan.getPrice());
        userSubscription.setStartDate(LocalDateTime.now());
        userSubscription.setEndDate(LocalDateTime.now().plusDays(subscriptionPlan.getDurationDays()-1));
        userSubscription.setActive(false);
        userSubscription.setCreatedAt(LocalDateTime.now());
        userSubscription.setUpdatedAt(LocalDateTime.now());
        userSubscription.setSubscriptionStatus(SubscriptionStatus.PENDING);
        userSubscription.setRazorpayOrderId(order.get("id"));


        UserSubscription saveUserSubscription = userSubscriptionRepository.save(userSubscription);

        InitiateResponse initiateResponse = new InitiateResponse();
        initiateResponse.setOrderId(order.get("id"));
        initiateResponse.setAmount(order.get("amount"));
        initiateResponse.setCurrency(order.get("currency"));
        initiateResponse.setKey(razorpayConfig.getKeyId());
        initiateResponse.setLocalSubscriptionId(saveUserSubscription.getUserSubscriptionId());

        return initiateResponse;

//       BuySubscriptionPlanResponse buySubscriptionPlanResponse = userSubscriptionPlanMapper.convertUserSubscriptionPlanIntoResponse(saveUserSubscription);
//
//       return buySubscriptionPlanResponse;
        } catch (RazorpayException e) {
            throw new PaymentProcessingException("Failed to create Razorpay order", e);
        }

    }


    @Transactional
    public boolean verifyAndActivateSubscription(String razorpayOrderId, String razorpayPaymentId, String razorpaySignature){
        String payload = razorpayOrderId + "|" + razorpayPaymentId;
        String generatedSignature = HmacUtils.hmacSha256Hex(razorpayConfig.getKeySecret(), payload);

        if (!generatedSignature.equals(razorpaySignature)) {
            return false;
        }

        UserSubscription subscription = userSubscriptionRepository.findByRazorpayOrderId(razorpayOrderId);
        if (subscription == null) return false;

        if(subscription.getSubscriptionStatus().equals(SubscriptionStatus.ACTIVE))return true;

        subscription.setRazorpayPaymentId(razorpayPaymentId);
        subscription.setRazorpaySignature(razorpaySignature);
        subscription.setSubscriptionStatus(SubscriptionStatus.ACTIVE);
        subscription.setActive(true);
        subscription.setUpdatedAt(LocalDateTime.now());

        userSubscriptionRepository.save(subscription);

        return true;
    }


    // fallback: when webhook arrives (same verification using webhook signature)
    @Transactional
    public void handlePaymentCaptured(String orderId, String paymentId, int amount) {
        UserSubscription sub = userSubscriptionRepository.findByRazorpayOrderId(orderId);
        if (sub == null) return;
        if (sub.getSubscriptionStatus().equals(SubscriptionStatus.ACTIVE)) return;

        sub.setRazorpayPaymentId(paymentId);
        sub.setSubscriptionStatus(SubscriptionStatus.ACTIVE);
        sub.setActive(true);
        sub.setUpdatedAt(LocalDateTime.now());
        userSubscriptionRepository.save(sub);
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
