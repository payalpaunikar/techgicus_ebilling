package com.example.techgicus_ebilling.techgicus_ebilling.service;


import com.example.techgicus_ebilling.techgicus_ebilling.dto.dashboardDto.DashboardSummaryDto;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.itemDto.ItemSaleSummaryDto;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.revenueDto.RevenueOverviewDto;
import com.example.techgicus_ebilling.techgicus_ebilling.exception.DashboardException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

@Service
public class DashboardService {

    private RevenueService revenueService;
    private ItemService itemService;
    private SaleOrderService saleOrderService;

    private static final Logger log = LoggerFactory.getLogger(DashboardService.class);

    @Autowired
    public DashboardService(RevenueService revenueService, ItemService itemService, SaleOrderService saleOrderService) {
        this.revenueService = revenueService;
        this.itemService = itemService;
        this.saleOrderService = saleOrderService;
    }

    public DashboardSummaryDto getDashboardSummary(Long companyId){
        DashboardSummaryDto dashboardSummaryDto = new DashboardSummaryDto();

        try{
            // 🔹 Run all async tasks in parallel
            CompletableFuture<Double> totalRevenueFuture = revenueService.getTotalRevenueOfCurrentYearAsync(companyId)
                    .exceptionally(ex -> {
                        log.error("Failed to fetch total revenue", ex);
                        return 0.0; // default fallback
                    });

            CompletableFuture<List<RevenueOverviewDto>> revenueOverviewFuture = revenueService.getRevenueOverviewAsync(companyId)
                    .exceptionally(ex -> {
                        log.error("Failed to fetch monthly revenue summary", ex);
                        return List.of();
                    });

            CompletableFuture<List<ItemSaleSummaryDto>>  itemSaleSummaryFuture = itemService.getItemSaleSummary(companyId)
                    .exceptionally(ex -> {
                        log.error("Failed to fetch item sale summary", ex);
                        return List.of();
                    });

            CompletableFuture<Double> totalSaleOrderFuture = saleOrderService.getTotalOrderOfCurrentYear(companyId)
                    .exceptionally(ex -> {
                        log.error("Failed to fetch total sale order", ex);
                        return 0.0; // default fallback
                    });

            CompletableFuture<Double> growthRateFuture = revenueService.getYearlyGrowthRate(companyId)
                    .exceptionally(ex -> {
                        log.error("Failed to fetch Growth Rate", ex);
                        return 0.0; // default fallback
                    });

            // 🔹 Wait until all are done
            CompletableFuture.allOf(
                    totalRevenueFuture,revenueOverviewFuture,itemSaleSummaryFuture,totalRevenueFuture,
                    growthRateFuture
            ).join();


            dashboardSummaryDto.setTotalRevenue(totalRevenueFuture.get());
            dashboardSummaryDto.setMonthlyRevenueSummary(revenueOverviewFuture.get());
            dashboardSummaryDto.setItemSaleSummary(itemSaleSummaryFuture.get());
            dashboardSummaryDto.setTotalSaleOrder(totalSaleOrderFuture.get());
            dashboardSummaryDto.setGrowthRate(growthRateFuture.get());

        }
        catch (CompletionException ce) {
            log.error("Async task failed while fetching dashboard summary", ce);
            throw new DashboardException("Error while fetching dashboard data"+" - "+ce);
        } catch (Exception e) {
            log.error("Unexpected error while building dashboard summary", e);
            throw new DashboardException("Unexpected error while fetching dashboard summary"+" - "+e);
        }

        return dashboardSummaryDto;
    }
}
