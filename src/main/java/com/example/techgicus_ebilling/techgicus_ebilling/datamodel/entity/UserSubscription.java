package com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity;


import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.PaymentStatus;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.PaymentType;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_subscriptions")
public class UserSubscription {

     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     private Long userSubscriptionId;

     @ManyToOne(fetch = FetchType.LAZY)
     @JoinColumn(name = "user_id",nullable = false)
     private User user;

     @ManyToOne(fetch = FetchType.LAZY)
     @JoinColumn(name = "subscription_plan_id",nullable = false)
     private SubscriptionPlan subscriptionPlan;

     // snapshot of plan at time of purchase (important!)
     private String nameAtPurchase;
     private BigDecimal priceAtPurchase;
     private Integer durationAtPurchase;

     //subscription tracking
     private LocalDateTime startDate;
     private LocalDateTime endDate;

     private Boolean isActive;
     private Boolean isEXpired;
     private Boolean isCancelled = false;


     @Enumerated(EnumType.STRING)
     private PaymentStatus paymentStatus;

     @Enumerated(EnumType.STRING)
     private PaymentType paymentType;


     private LocalDateTime createdAt;
     private LocalDateTime updatedAt;

     public UserSubscription() {
     }


     public UserSubscription(Long userSubscriptionId, User user, SubscriptionPlan subscriptionPlan, String nameAtPurchase, BigDecimal priceAtPurchase, Integer durationAtPurchase, LocalDateTime startDate, LocalDateTime endDate, Boolean isActive, Boolean isEXpired, Boolean isCancelled, PaymentStatus paymentStatus, PaymentType paymentType, LocalDateTime createdAt, LocalDateTime updatedAt) {
          this.userSubscriptionId = userSubscriptionId;
          this.user = user;
          this.subscriptionPlan = subscriptionPlan;
          this.nameAtPurchase = nameAtPurchase;
          this.priceAtPurchase = priceAtPurchase;
          this.durationAtPurchase = durationAtPurchase;
          this.startDate = startDate;
          this.endDate = endDate;
          this.isActive = isActive;
          this.isEXpired = isEXpired;
          this.isCancelled = isCancelled;
          this.paymentStatus = paymentStatus;
          this.paymentType = paymentType;
          this.createdAt = createdAt;
          this.updatedAt = updatedAt;
     }


     public Long getUserSubscriptionId() {
          return userSubscriptionId;
     }

     public void setUserSubscriptionId(Long userSubscriptionId) {
          this.userSubscriptionId = userSubscriptionId;
     }

     public User getUser() {
          return user;
     }

     public void setUser(User user) {
          this.user = user;
     }

     public SubscriptionPlan getSubscriptionPlan() {
          return subscriptionPlan;
     }

     public void setSubscriptionPlan(SubscriptionPlan subscriptionPlan) {
          this.subscriptionPlan = subscriptionPlan;
     }

     public String getNameAtPurchase() {
          return nameAtPurchase;
     }

     public void setNameAtPurchase(String nameAtPurchase) {
          this.nameAtPurchase = nameAtPurchase;
     }

     public BigDecimal getPriceAtPurchase() {
          return priceAtPurchase;
     }

     public void setPriceAtPurchase(BigDecimal priceAtPurchase) {
          this.priceAtPurchase = priceAtPurchase;
     }

     public Integer getDurationAtPurchase() {
          return durationAtPurchase;
     }

     public void setDurationAtPurchase(Integer durationAtPurchase) {
          this.durationAtPurchase = durationAtPurchase;
     }

     public LocalDateTime getStartDate() {
          return startDate;
     }

     public void setStartDate(LocalDateTime startDate) {
          this.startDate = startDate;
     }

     public LocalDateTime getEndDate() {
          return endDate;
     }

     public void setEndDate(LocalDateTime endDate) {
          this.endDate = endDate;
     }

     public Boolean getActive() {
          return isActive;
     }

     public void setActive(Boolean active) {
          isActive = active;
     }

     public Boolean getEXpired() {
          return isEXpired;
     }

     public void setEXpired(Boolean EXpired) {
          isEXpired = EXpired;
     }

     public Boolean getCancelled() {
          return isCancelled;
     }

     public void setCancelled(Boolean cancelled) {
          isCancelled = cancelled;
     }

     public PaymentStatus getPaymentStatus() {
          return paymentStatus;
     }

     public void setPaymentStatus(PaymentStatus paymentStatus) {
          this.paymentStatus = paymentStatus;
     }

     public PaymentType getPaymentType() {
          return paymentType;
     }

     public void setPaymentType(PaymentType paymentType) {
          this.paymentType = paymentType;
     }

     public LocalDateTime getCreatedAt() {
          return createdAt;
     }

     public void setCreatedAt(LocalDateTime createdAt) {
          this.createdAt = createdAt;
     }

     public LocalDateTime getUpdatedAt() {
          return updatedAt;
     }

     public void setUpdatedAt(LocalDateTime updatedAt) {
          this.updatedAt = updatedAt;
     }
}
