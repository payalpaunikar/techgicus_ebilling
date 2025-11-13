package com.example.techgicus_ebilling.techgicus_ebilling.dto.revenueDto;

public class RevenueOverviewDto {

    private Integer monthNumber;
    public String monthName;
    public Double totalRevenue;

    public RevenueOverviewDto() {
    }

    public RevenueOverviewDto(Integer monthNumber, String monthName, Double totalRevenue) {
        this.monthNumber = monthNumber;
        this.monthName = monthName;
        this.totalRevenue = totalRevenue;
    }

    public Integer getMonthNumber() {
        return monthNumber;
    }

    public void setMonthNumber(Integer monthNumber) {
        this.monthNumber = monthNumber;
    }

    public String getMonthName() {
        return monthName;
    }

    public void setMonthName(String monthName) {
        this.monthName = monthName;
    }

    public Double getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(Double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }
}
