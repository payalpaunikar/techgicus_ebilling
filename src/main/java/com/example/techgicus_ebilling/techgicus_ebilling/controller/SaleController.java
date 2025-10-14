package com.example.techgicus_ebilling.techgicus_ebilling.controller;


import com.example.techgicus_ebilling.techgicus_ebilling.dto.saleDto.SalePaymentRequest;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.saleDto.SalePaymentResponse;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.saleDto.SaleRequest;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.saleDto.SaleResponse;
import com.example.techgicus_ebilling.techgicus_ebilling.service.SaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
public class SaleController {

      private SaleService saleService;

    @Autowired
    public SaleController(SaleService saleService) {
        this.saleService = saleService;
    }


    @PostMapping("/company/{companyId}/create-sale")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<SaleResponse> createSale(@RequestBody SaleRequest saleRequest, @PathVariable Long companyId){
        return ResponseEntity.status(HttpStatus.CREATED).body(saleService.createdSale(saleRequest,companyId));
    }


    @GetMapping("/company/{companyId}/sales")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<SaleResponse>> getCompanySales(@PathVariable Long companyId){
       return ResponseEntity.ok(saleService.getComapnySales(companyId));
    }


    @GetMapping("/sale/{saleId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<SaleResponse> getSaleById(@PathVariable Long saleId){
      return ResponseEntity.ok(saleService.getSaleById(saleId));
    }


    @PutMapping("/sale/{saleId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<SaleResponse> updateSaleById(@PathVariable Long saleId,@RequestBody SaleRequest saleRequest){
        return ResponseEntity.ok(saleService.updateSaleById(saleId,saleRequest));
    }


    @DeleteMapping("/sale/{saleId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> deleteSaleById(@PathVariable Long saleId){
        return ResponseEntity.ok(saleService.deleteSaleById(saleId));
    }


    @PostMapping("/sale/{saleId}/add-payment")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<SalePaymentResponse> addSalePayment(@PathVariable Long saleId, @RequestBody SalePaymentRequest salePaymentRequest){
        return ResponseEntity.ok(saleService.addSalePayment(saleId,salePaymentRequest));
    }



    @GetMapping("/company/{companyId}/sale/report/by/filter")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<SaleResponse> getSaleReportByFilter(@PathVariable Long companyId,
                                                    @RequestParam(required = false) Long partyId,
                                                    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)LocalDate startDate,
                                                    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)LocalDate endDate){
       return saleService.getSaleReportByFilter(companyId,partyId,startDate,endDate);
    }

}
