package com.example.techgicus_ebilling.techgicus_ebilling.controller;


import com.example.techgicus_ebilling.techgicus_ebilling.dto.purchaseDto.PurchasePaymentRequest;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.purchaseDto.PurchasePaymentResponse;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.purchaseDto.PurchaseRequest;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.purchaseDto.PurchaseResponse;
import com.example.techgicus_ebilling.techgicus_ebilling.service.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.method.P;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
public class PurchaseController {

      private PurchaseService purchaseService;

    @Autowired
    public PurchaseController(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }


    @PostMapping("/company/{companyId}/create-purchase")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<PurchaseResponse> createPurchase(@PathVariable Long companyId, @RequestBody PurchaseRequest purchaseRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(purchaseService.createdPurchase(companyId,purchaseRequest));
    }


    @GetMapping("/company/{companyId}/purchases")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<PurchaseResponse>> getCompanyPurchases(@PathVariable Long companyId){
        return ResponseEntity.ok(purchaseService.getCompanyPurchases(companyId));
    }


    @GetMapping("/purchase/{purchaseId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<PurchaseResponse> getPurchaseById(@PathVariable Long purchaseId){
        return ResponseEntity.ok(purchaseService.getPurchaseById(purchaseId));
    }


    @PutMapping("/purchase/{purchaseId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<PurchaseResponse> updatePurchaseById(@PathVariable Long purchaseId,@RequestBody PurchaseRequest purchaseRequest){
        return ResponseEntity.ok(purchaseService.updatePurchaseById(purchaseId,purchaseRequest));
    }


    @DeleteMapping("/purchase/{purchaseId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> deletePurchaseById(@PathVariable Long purchaseId){
        return ResponseEntity.ok(purchaseService.deletePurchaseById(purchaseId));
    }


    @PostMapping("/purchase/{purchaseId}/make-payment")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<PurchasePaymentResponse> makePurchasePayment(@PathVariable Long purchaseId,
                                                                       @RequestBody PurchasePaymentRequest purchasePaymentRequest){
        return ResponseEntity.ok(purchaseService.makePurchasePayment(purchaseId,purchasePaymentRequest));
    }


    @GetMapping("/company/{companyId}/purchase/report/by/filter")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<PurchaseResponse> getPurchaseReportByFilter(@PathVariable Long companyId,
                                                            @RequestParam(required = false) Long partyId,
                                                            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)LocalDate endDate){
        return purchaseService.getPurchaseReportByFilter(companyId,partyId,startDate,endDate);
    }
}
