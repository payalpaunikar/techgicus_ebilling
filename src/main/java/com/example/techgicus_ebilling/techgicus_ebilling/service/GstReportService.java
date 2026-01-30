package com.example.techgicus_ebilling.techgicus_ebilling.service;


import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Company;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Purchase;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Sale;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.SaleItem;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.TaxRate;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.gstrDto.*;
import com.example.techgicus_ebilling.techgicus_ebilling.exception.BadRequestException;
import com.example.techgicus_ebilling.techgicus_ebilling.exception.ResourceNotFoundException;
import com.example.techgicus_ebilling.techgicus_ebilling.repository.CompanyRepository;
import com.example.techgicus_ebilling.techgicus_ebilling.repository.PurchaseRepository;
import com.example.techgicus_ebilling.techgicus_ebilling.repository.SaleRepository;
import com.example.techgicus_ebilling.techgicus_ebilling.service.processor.Gstr1SalesProcessor;
import com.example.techgicus_ebilling.techgicus_ebilling.service.processor.Gstr2PurchaseProcessor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class GstReportService extends GstrBaseService{

    private final SaleRepository saleRepository;
    private final CompanyRepository companyRepository;
    private final Gstr1SalesProcessor gstr1SalesProcessor;
    private final Gstr2PurchaseProcessor gstr2PurchaseProcessor;
    private final PurchaseRepository purchaseRepository;

    public GstReportService(SaleRepository saleRepository, CompanyRepository companyRepository, Gstr1SalesProcessor gstr1SalesProcessor, Gstr2PurchaseProcessor gstr2PurchaseProcessor, PurchaseRepository purchaseRepository) {
        this.saleRepository = saleRepository;
        this.companyRepository = companyRepository;
        this.gstr1SalesProcessor = gstr1SalesProcessor;
        this.gstr2PurchaseProcessor = gstr2PurchaseProcessor;
        this.purchaseRepository = purchaseRepository;
    }

    public GstrDto generateGSTR1Report(Long companyId,
                                       String startMonthYear,
                                       String endMonthYear){

         Company company = getCompany(companyId, companyRepository);
         LocalDate startDate = parseFirstDay(startMonthYear);
         LocalDate endDate = parseLastDay(endMonthYear);

         GstrDto report = initReport(company, startMonthYear, endMonthYear);
         report.setPeriod(formatPeriod(startDate, endDate));

         List<Sale> sales =
                 saleRepository.findForGstr1(companyId, startDate, endDate);

         String companyStateCode = company.getState().getCode();
         gstr1SalesProcessor.process(report,sales,companyStateCode);

          return report;
     }


    public GstrDto generateGSTR2Report(Long companyId,
                                       String startMonthYear,
                                       String endMonthYear){

        Company company = getCompany(companyId, companyRepository);
        LocalDate startDate = parseFirstDay(startMonthYear);
        LocalDate endDate = parseLastDay(endMonthYear);

        GstrDto report = initReport(company, startMonthYear, endMonthYear);
        report.setPeriod(formatPeriod(startDate, endDate));

       List<Purchase> purchases = purchaseRepository.findForGstr2(companyId,startDate,endDate);

        String companyStateCode = company.getState().getCode();

        gstr2PurchaseProcessor.process(report,purchases,companyStateCode);

        return report;
    }



}






