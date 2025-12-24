package com.example.techgicus_ebilling.techgicus_ebilling.controller;


import com.example.techgicus_ebilling.techgicus_ebilling.dto.reportDto.*;
import com.example.techgicus_ebilling.techgicus_ebilling.service.ReportService;
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
                                                             @RequestParam(defaultValue = "Year") String period,
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
                                                                     @RequestParam(defaultValue = "Year") String period,
                                                                     @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)LocalDate startDate,
                                                                     @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                                                                     @RequestParam(required = false) String purchaseTxnType
    ){
        if(purchaseTxnType !=null){
            purchaseTxnType = purchaseTxnType.toUpperCase();
        }
        return ResponseEntity.ok(reportService.gePurchaseReport(period,startDate,endDate,partyId,purchaseTxnType,companyId));
    }




    @GetMapping("/party/{partyId}/statement/list")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<PartyStatement> getPartyStatementList(@PathVariable Long partyId,
                                                      @RequestParam(defaultValue = "Year") String period,
                                                      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)LocalDate startDate,
                                                      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
                                                      ){

        return reportService.getPartyStatementList(period,startDate,endDate,partyId);

    }



    @GetMapping("/company/{companyId}/parties/report")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<PartyReport> getAllPartyReports(@PathVariable Long companyId){
        return reportService.getAllPartyReports(companyId);
    }



    @GetMapping("/company/{companyId}/profit-loss/report")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ProfitAndLossReport generateProfitAndLossReport(@PathVariable Long companyId,
                                                           @RequestParam(defaultValue = "Year") String period,
                                                           @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)LocalDate startDate,
                                                           @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate){

        return reportService.generateProfitAndLoss(companyId,period,startDate,endDate);
    }


    @GetMapping("/company/{companyId}/bill-wise/profit-loss/report")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<BillWiseProfitAndLossSummary> generateBillWiseProfitAndLossReport(@PathVariable Long companyId,
                                                                                  @RequestParam(defaultValue = "Year") String period,
                                                                                  @RequestParam(required = false) Long partyId,
                                                                                  @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)LocalDate startDate,
                                                                                  @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate){

        return reportService.generatedBillWiseProfitAndLossReport(period,partyId,companyId,startDate,endDate);
    }



    @GetMapping("/sale/{saleId}/bill-wise/profit-loss/details")
    @PreAuthorize("hasAuthority('ADMIN')")
    public BillWiseProfitAndLossDetail getBillWiseProfitAndLossDetailsBySaleId(@PathVariable Long saleId){
        return reportService.getBillWiseProfitAndLossDetailBySaleId(saleId);
    }

}
