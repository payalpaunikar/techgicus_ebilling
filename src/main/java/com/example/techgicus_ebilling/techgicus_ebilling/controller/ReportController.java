package com.example.techgicus_ebilling.techgicus_ebilling.controller;


import com.example.techgicus_ebilling.techgicus_ebilling.dto.reportDto.PurchaseReportDto;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.reportDto.SaleReportDto;
import com.example.techgicus_ebilling.techgicus_ebilling.service.ReportService;
import com.example.techgicus_ebilling.techgicus_ebilling.service.SaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
public class ReportController {

     public ReportService reportService;

    @Autowired
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }


    @GetMapping("/company/{companyId}/sale/report")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<SaleReportDto>> getSaleReport(@PathVariable Long companyId,
                                                             @RequestParam(required = false) Long partyId,
                                                             @RequestParam String period,
                                                             @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)LocalDate startDate,
                                                             @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                                                             @RequestParam(required = false) String saleTxnType
                                                             ){
        if(saleTxnType !=null){
           saleTxnType = saleTxnType.toUpperCase();
        }
        return ResponseEntity.ok(reportService.getSaleReport(period,startDate,endDate,partyId,saleTxnType,companyId));
    }

    @GetMapping("/company/{companyId}/purchase/report")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<PurchaseReportDto>> getPurchaseReport(@PathVariable Long companyId,
                                                                     @RequestParam(required = false) Long partyId,
                                                                     @RequestParam String period,
                                                                     @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)LocalDate startDate,
                                                                     @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                                                                     @RequestParam(required = false) String purchaseTxnType
    ){
        if(purchaseTxnType !=null){
            purchaseTxnType = purchaseTxnType.toUpperCase();
        }
        return ResponseEntity.ok(reportService.gePurchaseReport(period,startDate,endDate,partyId,purchaseTxnType,companyId));
    }



}
