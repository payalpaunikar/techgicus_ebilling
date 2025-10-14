package com.example.techgicus_ebilling.techgicus_ebilling.controller;


import com.example.techgicus_ebilling.techgicus_ebilling.dto.paymentInDto.PaymentInRequest;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.paymentInDto.PaymentInResponse;
import com.example.techgicus_ebilling.techgicus_ebilling.service.PaymentInService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
public class PaymentInController {

        private PaymentInService paymentInService;

    @Autowired
    public PaymentInController(PaymentInService paymentInService) {
        this.paymentInService = paymentInService;
    }


    @PostMapping("/company/{companyId}/add/payment-in")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<PaymentInResponse> addPaymentIn(@PathVariable Long companyId, @RequestBody PaymentInRequest paymentInRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(paymentInService.addPaymentIn(companyId,paymentInRequest));
    }


    @GetMapping("/company/{companyId}/payment-in/list/by/filter")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<PaymentInResponse> getPaymentInListByFilter(@PathVariable Long companyId,
                                                            @RequestParam(required = false) Long partyId,
                                                            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)LocalDate startDate,
                                                            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate){
        return paymentInService.getAllPaymentInByFilter(companyId,partyId,startDate,endDate);
    }



    @GetMapping("/paymentIn/{paymentInId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public PaymentInResponse getPaymentInById(@PathVariable Long paymentInId){
        return paymentInService.getPaymentInById(paymentInId);
    }


    @PutMapping("/paymentIn/{paymentInId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public PaymentInResponse updatePaymentInById(@PathVariable Long paymentInId,@RequestBody PaymentInRequest paymentInRequest){
        return paymentInService.updatePaymentInById(paymentInId,paymentInRequest);
    }

    @DeleteMapping("/paymentIn/{paymentInId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String deletePaymentInById(@PathVariable Long paymentInId){
        return paymentInService.deletePaymentInById(paymentInId);
    }
}
