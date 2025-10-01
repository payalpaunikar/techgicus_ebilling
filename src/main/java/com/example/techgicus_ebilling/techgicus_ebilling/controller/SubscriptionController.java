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
    public ResponseEntity<BuySubscriptionPlanResponse> buySubscriptionPlan(@RequestBody BuySubscriptionPlanRequest buySubscriptionPlanRequest){
      return ResponseEntity.ok(subscriptionService.buySubscription(buySubscriptionPlanRequest));
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
