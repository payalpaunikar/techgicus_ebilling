package com.example.techgicus_ebilling.techgicus_ebilling.service;

import com.example.techgicus_ebilling.techgicus_ebilling.dto.revenueDto.MonthlyRevenue;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.revenueDto.RevenueOverviewDto;
import com.example.techgicus_ebilling.techgicus_ebilling.repository.SaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class RevenueService {

       private SaleRepository saleRepository;

    @Autowired
    public RevenueService(SaleRepository saleRepository) {
        this.saleRepository = saleRepository;
    }

    @Async
       public CompletableFuture<Double> getTotalRevenueOfCurrentYearAsync(Long companyId){
//           Double totalRevenueOfCurrentYear = saleRepository.findCurrentYearRevenue(companyId);

        Double totalRevenueOfCurrentYear = Optional.ofNullable(
                saleRepository.findCurrentYearRevenue(companyId)
        ).orElse(0.0);

        return CompletableFuture.completedFuture(totalRevenueOfCurrentYear);
       }


       @Async
       public CompletableFuture<List<RevenueOverviewDto>> getRevenueOverviewAsync(Long companyId){
           List<MonthlyRevenue> monthlyRevenuesOfCurrrentYear = saleRepository.findMonthlyRevenueForCurrentYear(companyId);

           List<RevenueOverviewDto> revenueOverviewDtos = monthlyRevenuesOfCurrrentYear.stream()
                   .map(monthlyRevenue -> {
                       RevenueOverviewDto revenueOverviewDto = new RevenueOverviewDto();
                       revenueOverviewDto.setMonthNumber(monthlyRevenue.getMonthNumber());
                       revenueOverviewDto.setMonthName(monthlyRevenue.getMonthName());
                       revenueOverviewDto.setTotalRevenue(monthlyRevenue.getTotalRevenue());

                       return  revenueOverviewDto;
                   }).toList();

           return CompletableFuture.completedFuture(revenueOverviewDtos);
       }


       @Async
       public CompletableFuture<Double> getYearlyGrowthRate(Long companyId){
           LocalDate currentYearStartDate = LocalDate.of(LocalDate.now().getYear(),1,1);
           LocalDate currentYearEndDate = LocalDate.of(LocalDate.now().getYear(), 12,31);
         Double currentYearRevenue = Optional.ofNullable(
                 saleRepository.findRevenueByDate(companyId,currentYearStartDate,currentYearEndDate)
         ).orElse(0.0);

           LocalDate previousYearStartDate = LocalDate.of(LocalDate.now().getYear()-1,1,1);
           LocalDate previousYearEndDate = LocalDate.of(LocalDate.now().getYear()-1, 12,31);
           Double previousYearRevenue = Optional.ofNullable(
                   saleRepository.findRevenueByDate(companyId,previousYearStartDate,previousYearEndDate)
           ).orElse(0.0);

           double growthRate = 0.0;
           if(previousYearRevenue >0){
               growthRate = ((currentYearRevenue - previousYearRevenue) / previousYearRevenue) * 100;
           }
           if (previousYearRevenue == 0 && currentYearRevenue>0){
              growthRate = 100.0;
           }
           if (previousYearRevenue == 0 && currentYearRevenue==0){
               growthRate = 0.0;
           }

           return CompletableFuture.completedFuture(growthRate);
       }

}
