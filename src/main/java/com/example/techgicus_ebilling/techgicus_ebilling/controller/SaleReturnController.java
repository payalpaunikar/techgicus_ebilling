package com.example.techgicus_ebilling.techgicus_ebilling.controller;


import com.example.techgicus_ebilling.techgicus_ebilling.dto.saleReturnDto.SaleReturnRequest;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.saleReturnDto.SaleReturnResponse;
import com.example.techgicus_ebilling.techgicus_ebilling.service.SaleReturnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
public class SaleReturnController {

       private SaleReturnService saleReturnService;

    @Autowired
    public SaleReturnController(SaleReturnService saleReturnService) {
        this.saleReturnService = saleReturnService;
    }

    @PostMapping("/company/{companyId}/create/sale-return")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<SaleReturnResponse> createSaleReturn(@PathVariable Long companyId, @RequestBody SaleReturnRequest saleReturnRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(saleReturnService.createdSaleReturn(companyId,saleReturnRequest));
    }


    @GetMapping("/company/{companyId}/get/sale-return/list/according")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<SaleReturnResponse>> getSaleReturnListByFilter(@PathVariable Long companyId,
                                                              @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)LocalDate startDate,
                                                              @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)LocalDate endDate){
        return ResponseEntity.ok(saleReturnService.getSaleReturnListByFilter(companyId,startDate,endDate));
    }


    @GetMapping("/sale-return/{saleReturnId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public SaleReturnResponse getSaleReturnById(@PathVariable Long saleReturnId){
        return saleReturnService.getSaleReturnById(saleReturnId);
    }

    @PutMapping("/sale-return/{saleReturnId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public SaleReturnResponse updateSaleReturnById(@PathVariable Long saleReturnId,@RequestBody SaleReturnRequest saleReturnRequest){
        return saleReturnService.updateSaleReturnById(saleReturnId,saleReturnRequest);
    }

    @DeleteMapping("/sale-return/{saleReturnId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> deleteSaleReturnById(@PathVariable Long saleReturnId){
        return ResponseEntity.ok(saleReturnService.deleteSaleReturnById(saleReturnId));
    }

}
