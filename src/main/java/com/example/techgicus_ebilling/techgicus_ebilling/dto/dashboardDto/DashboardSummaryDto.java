package com.example.techgicus_ebilling.techgicus_ebilling.dto.dashboardDto;

import com.example.techgicus_ebilling.techgicus_ebilling.dto.itemDto.ItemSaleSummaryDto;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.revenueDto.RevenueOverviewDto;

import java.util.List;

public class DashboardSummaryDto {
    private Double totalRevenue;
    private Double totalSaleOrder;
    private Double growthRate;
    private List<RevenueOverviewDto> monthlyRevenueSummary;
    private List<ItemSaleSummaryDto> itemSaleSummary;


    public Double getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(Double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public List<RevenueOverviewDto> getMonthlyRevenueSummary() {
        return monthlyRevenueSummary;
    }

    public void setMonthlyRevenueSummary(List<RevenueOverviewDto> monthlyRevenueSummary) {
        this.monthlyRevenueSummary = monthlyRevenueSummary;
    }

    public List<ItemSaleSummaryDto> getItemSaleSummary() {
        return itemSaleSummary;
    }

    public void setItemSaleSummary(List<ItemSaleSummaryDto> itemSaleSummary) {
        this.itemSaleSummary = itemSaleSummary;
    }

    public Double getTotalSaleOrder() {
        return totalSaleOrder;
    }

    public void setTotalSaleOrder(Double totalSaleOrder) {
        this.totalSaleOrder = totalSaleOrder;
    }

    public Double getGrowthRate() {
        return growthRate;
    }

    public void setGrowthRate(Double growthRate) {
        this.growthRate = growthRate;
    }
}
