package com.example.techgicus_ebilling.techgicus_ebilling.controller;


import com.example.techgicus_ebilling.techgicus_ebilling.dto.purchaseReturnDto.PurchaseReturnRequest;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.purchaseReturnDto.PurchaseReturnResponse;
import com.example.techgicus_ebilling.techgicus_ebilling.service.PurchaseReturnService;
import jakarta.persistence.GeneratedValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
public class PurchaseReturnController {

     private PurchaseReturnService purchaseReturnService;

    @Autowired
    public PurchaseReturnController(PurchaseReturnService purchaseReturnService) {
        this.purchaseReturnService = purchaseReturnService;
    }


    @PostMapping("/company/{companyId}/create/purchase-return")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<PurchaseReturnResponse> createPurchaseReturn(@PathVariable Long companyId, @RequestBody PurchaseReturnRequest purchaseReturnRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(purchaseReturnService.createPurchaseReturn(companyId,purchaseReturnRequest));
    }


    @GetMapping("/company/{companyId}/get/purchase/return/list/according")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<PurchaseReturnResponse> getPurchaseReturnListByFilter(@PathVariable Long companyId,
                                                                      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)LocalDate startDate,
                                                                      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)LocalDate endDate){
        return purchaseReturnService.getPurchaseReturnListByFilter(companyId,startDate,endDate);
    }


    @GetMapping("/purchase-return/{purchaseReturnId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public PurchaseReturnResponse getPurchaseReturnById(@PathVariable Long purchaseReturnId){
        return purchaseReturnService.getPurchaseReturnById(purchaseReturnId);
    }

    @PutMapping("/purchase-return/{purchaseReturnId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public PurchaseReturnResponse updatePurchaseReturnById(@PathVariable Long purchaseReturnId,@RequestBody PurchaseReturnRequest purchaseReturnRequest){
        return purchaseReturnService.updatePurchaseReturnById(purchaseReturnId,purchaseReturnRequest);
    }

    @DeleteMapping("/purchase-return/{purchaseReturnId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> deletePurchaseReturnById(@PathVariable Long purchaseReturnId){
        return ResponseEntity.ok(purchaseReturnService.deletePurchaseReturnById(purchaseReturnId));
    }
}
