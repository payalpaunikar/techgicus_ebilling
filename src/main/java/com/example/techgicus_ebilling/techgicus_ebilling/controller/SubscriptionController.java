package com.example.techgicus_ebilling.techgicus_ebilling.controller;


import com.example.techgicus_ebilling.techgicus_ebilling.dto.subscriptionPlanDto.*;
import com.example.techgicus_ebilling.techgicus_ebilling.security.MyUserDetails;
import com.example.techgicus_ebilling.techgicus_ebilling.service.SubscriptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.extensions.Extension;
import io.swagger.v3.oas.annotations.extensions.ExtensionProperty;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@Tag(name = "Subscription Api")
public class SubscriptionController {

      private SubscriptionService subscriptionService;

      private final static Logger logger = LoggerFactory.getLogger(SubscriptionController.class);

    @Autowired
    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }



    @Operation(summary = "Get subscriptions with their specific feature",
            description = "Using this api we get subscriptions plan with their specific features",
            security = {@SecurityRequirement(name = "bearerAuth")},
            extensions = {
            @Extension(name = "x-roles",properties = {
                    @ExtensionProperty(name = "allowedRoles",value = "ADMIN,SUPERADMIN")
            })
            }
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200",description = "Get all subscriptions",
                            content = @Content(
                                    mediaType = "application/json",
                                   // schema = @Schema(implementation = SubscriptionPlanWithFeatures.class)
                                    array = @ArraySchema(schema = @Schema(implementation = SubscriptionPlanWithFeatures.class))
                            ))
            }
    )
    @GetMapping("/get/subscription-plan")
    @PreAuthorize("hasAnyAuthority('ADMIN','SUPERADMIN')")
    public ResponseEntity<List<SubscriptionPlanWithFeatures>> getSubscriptionPlansWithFeatures(Authentication authentication){
        logger.info("Inside the subscriptioncontroller");
        MyUserDetails myUserDetails =(MyUserDetails) authentication.getPrincipal();
       return ResponseEntity.ok(subscriptionService.getSubscriptionPlansWithFeatures(myUserDetails.getUser()));
    }



    @Operation(summary = "Buy subscription api",description = "This api is used for buy the subscription plan",
            security = {@SecurityRequirement(name = "bearerAuth")},
            extensions = {
                    @Extension(name = "x-roles",properties = {
                            @ExtensionProperty(name = "allowedRoles",value = "ADMIN,SUPERADMIN")
                    })
            })
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "User buy subscription succefully",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = BuySubscriptionPlanResponse.class))
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Resource not found exception (like user id , subscription id)",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = BuySubscriptionPlanResponse.class))
                    )
            )
    })
    @PostMapping("/buy/subscription-plan")
    @PreAuthorize("hasAnyAuthority('ADMIN','SUPERADMIN')")
    public ResponseEntity<InitiateResponse> buySubscriptionPlan(@RequestBody BuySubscriptionPlanRequest buySubscriptionPlanRequest){
      return ResponseEntity.ok(subscriptionService.initiateBuySubscription(buySubscriptionPlanRequest));
    }


    // Frontend calls this after Razorpay checkout handler returns razorpay_* values
    @PostMapping("/verify-payment")
    public ResponseEntity<Map<String,String>> verifyPayment(@RequestBody Map<String, String> payload) {
        String orderId = payload.get("razorpay_order_id");
        String paymentId = payload.get("razorpay_payment_id");
        String signature = payload.get("razorpay_signature");

        boolean ok = subscriptionService.verifyAndActivateSubscription(orderId, paymentId, signature);
        if (ok) return ResponseEntity.ok(Map.of("status", "success"));
        return ResponseEntity.status(400).body(Map.of("status", "failure"));
    }

    @Operation(summary = "check user subscription is active or not",
            security = {@SecurityRequirement(name = "bearerAuth")},
            extensions = {
                    @Extension(name = "x-roles",properties = {
                            @ExtensionProperty(name = "allowedRoles",value = "ADMIN,SUPERADMIN")
                    })
            })
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "get the status of the user subscription",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = BuySubscriptionPlanResponse.class))
                    )
            )
    })
    @GetMapping("/user/{userId}/subscription/isActive")
    @PreAuthorize("hasAnyAuthority('ADMIN','SUPERADMIN')")
    public ResponseEntity<SubscriptionStatusResponse> isUserSubscriptionActive(
            @Parameter(description = "userId= user id of the user",example = "1")
            @PathVariable Long userId){
       return ResponseEntity.ok(subscriptionService.isUserSubscriptionActive(userId));
    }


    @GetMapping("/user/{userId}/current-plan")
    @PreAuthorize("hasAnyAuthority('ADMIN','SUPERADMIN')")
    public ResponseEntity<BuySubscriptionPlanResponse> userCurrentPlan(@PathVariable Long userId){
       return ResponseEntity.ok(subscriptionService.userCurrentPlan(userId));
    }



    @GetMapping("/user/{userId}/current-plan/available-plan")
    @PreAuthorize("hasAnyAuthority('ADMIN','SUPERADMIN')")
    public CurrentAndAvailablePlanResponse userCurrentAndAvailablePlan(@PathVariable Long userId){
        return subscriptionService.currentAndAvailablePlan(userId);
    }


    @PutMapping("/user/{userId}/update-plan")
    @PreAuthorize("hasAnyAuthority('ADMIN','SUPERADMIN')")
    public ResponseEntity<BuySubscriptionPlanResponse> updateUserSubscription(
            @RequestBody UpdateUserSubscriptionRequest updateUserSubscriptionRequest,
            @PathVariable Long userId){
     return  ResponseEntity.ok(subscriptionService.updateUserSubscription(userId,updateUserSubscriptionRequest));
    }



    @GetMapping("/get/subscription-plan/{subscriptionPlanId}")
    @PreAuthorize("hasAuthority('SUPERADMIN')")
    public ResponseEntity<SubscriptionPlanResponse> getSubscriptionById(@PathVariable Long subscriptionPlanId){
        return ResponseEntity.ok(subscriptionService.getSubscriptionById(subscriptionPlanId));
    }

}
