package com.example.techgicus_ebilling.techgicus_ebilling.controller;


import com.example.techgicus_ebilling.techgicus_ebilling.dto.quotationDto.QuotationRequest;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.quotationDto.QuotationResponse;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.saleDto.SaleRequest;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.saleDto.SaleResponse;
import com.example.techgicus_ebilling.techgicus_ebilling.service.QuotationService;
import org.apache.juli.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class QuotationController {

      private QuotationService quotationService;

    @Autowired
    public QuotationController(QuotationService quotationService) {
        this.quotationService = quotationService;
    }


    @PostMapping("/company/{companyId}/create-quotation")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<QuotationResponse> createdQuotation(@PathVariable Long companyId,@RequestBody QuotationRequest quotationRequest){
      return ResponseEntity.status(HttpStatus.CREATED).body(quotationService.createdQuotation(companyId,quotationRequest));
    }


    @GetMapping("/quotation/{quotationId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<QuotationResponse> getQuotationById(@PathVariable Long quotationId){
      return ResponseEntity.ok(quotationService.getQuotationById(quotationId));
    }


    @GetMapping("/company/{companyId}/quotations")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<QuotationResponse> getAllQuotationByCompanyId(@PathVariable Long companyId){
        return quotationService.getAllQuotation(companyId);
    }


    @PutMapping("/quotation/{quotationId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public QuotationResponse updateQuotationById(@PathVariable Long quotationId,@RequestBody QuotationRequest quotationRequest){
        return quotationService.updateQuotationById(quotationId,quotationRequest);
    }


    @DeleteMapping("/quotation/{quotationId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> deleteQuotationById(@PathVariable Long quotationId){
        return ResponseEntity.ok(quotationService.deleteQuotationById(quotationId));
    }


    @PostMapping("/quotation/{quotationId}/convert-to-sale")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<SaleResponse> convertQuotationToSale(@PathVariable Long quotationId, @RequestBody SaleRequest saleRequest){
        return ResponseEntity.ok(quotationService.convertQuotationToSale(quotationId,saleRequest));
    }
}
