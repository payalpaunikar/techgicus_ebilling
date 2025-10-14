package com.example.techgicus_ebilling.techgicus_ebilling.controller;


import com.example.techgicus_ebilling.techgicus_ebilling.dto.paymentInDto.PaymentInRequest;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.paymentInDto.PaymentInResponse;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.paymentOutDto.PaymentOutRequest;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.paymentOutDto.PaymentOutResponse;
import com.example.techgicus_ebilling.techgicus_ebilling.service.PaymentOutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
public class PaymentOutController {

     private PaymentOutService paymentOutService;

     @Autowired
    public PaymentOutController(PaymentOutService paymentOutService) {
        this.paymentOutService = paymentOutService;
    }

    @PostMapping("/company/{companyId}/add/payment-out")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<PaymentOutResponse> addPaymentOut(@PathVariable Long companyId, @RequestBody PaymentOutRequest paymentOutRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(paymentOutService.addPaymentOut(companyId,paymentOutRequest));
    }

    @GetMapping("/company/{companyId}/payment-out/list/by/filter")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<PaymentOutResponse> getPaymentOutListByFilter(@PathVariable Long companyId,
                                                            @RequestParam(required = false) Long partyId,
                                                            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate){
        return paymentOutService.getAllPaymentOutByFilter(companyId,partyId,startDate,endDate);
    }

    @GetMapping("/paymentOut/{paymentOutId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public PaymentOutResponse getPaymentOutById(@PathVariable Long paymentOutId){
        return paymentOutService.getPaymentOutById(paymentOutId);
    }


    @PutMapping("/paymentOut/{paymentOutId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public PaymentOutResponse updatePaymentOutById(@PathVariable Long paymentOutId,@RequestBody PaymentOutRequest paymentOutRequest){
        return paymentOutService.updatePaymentOutById(paymentOutId,paymentOutRequest);
    }

    @DeleteMapping("/paymentOut/{paymentOutId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String deletePaymentOutById(@PathVariable Long paymentOutId){
        return paymentOutService.deletePaymentOutById(paymentOutId);
    }

}
