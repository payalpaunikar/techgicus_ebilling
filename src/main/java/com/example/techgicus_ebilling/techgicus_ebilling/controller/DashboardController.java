package com.example.techgicus_ebilling.techgicus_ebilling.controller;


import com.example.techgicus_ebilling.techgicus_ebilling.dto.dashboardDto.DashboardSummaryDto;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.itemDto.ItemSaleSummaryDto;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.revenueDto.RevenueOverviewDto;
import com.example.techgicus_ebilling.techgicus_ebilling.service.DashboardService;
import com.example.techgicus_ebilling.techgicus_ebilling.service.ItemService;
import com.example.techgicus_ebilling.techgicus_ebilling.service.RevenueService;
import com.example.techgicus_ebilling.techgicus_ebilling.service.SaleOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
public class DashboardController {

      private DashboardService dashboardService;

    @Autowired
    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }


    @GetMapping("/company/{companyId}/dashboard")
    public ResponseEntity<DashboardSummaryDto> getDashboardSummary(@PathVariable Long companyId){
        return ResponseEntity.ok(dashboardService.getDashboardSummary(companyId));
    }
}
