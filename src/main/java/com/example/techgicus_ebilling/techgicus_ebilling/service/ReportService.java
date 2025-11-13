package com.example.techgicus_ebilling.techgicus_ebilling.service;


import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Company;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.PurchaseTransactionType;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.SaleTransactionType;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.reportDto.PurchaseReportDto;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.reportDto.SaleReportDto;
import com.example.techgicus_ebilling.techgicus_ebilling.exception.ResourceNotFoundException;
import com.example.techgicus_ebilling.techgicus_ebilling.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class ReportService {


        private SaleRepository saleRepository;
        private PurchaseRepository purchaseRepository;
        private SaleItemRepository saleItemRepository;
        private PurchaseItemRepository purchaseItemRepository;
        private CompanyRepository companyRepository;
        private SaleReturnRepository saleReturnRepository;
        private PurchaseReturnRepository purchaseReturnRepository;

    @Autowired
    public ReportService(SaleRepository saleRepository, PurchaseRepository purchaseRepository, SaleItemRepository saleItemRepository, PurchaseItemRepository purchaseItemRepository, CompanyRepository companyRepository, SaleReturnRepository saleReturnRepository, PurchaseReturnRepository purchaseReturnRepository) {
        this.saleRepository = saleRepository;
        this.purchaseRepository = purchaseRepository;
        this.saleItemRepository = saleItemRepository;
        this.purchaseItemRepository = purchaseItemRepository;
        this.companyRepository = companyRepository;
        this.saleReturnRepository = saleReturnRepository;
        this.purchaseReturnRepository = purchaseReturnRepository;
    }

    public List<SaleReportDto> getSaleReport(String period, LocalDate startDate, LocalDate endDate,
                                             Long partyId, String saleTxnsType,
                                             Long companyId){

            Company company = companyRepository.findById(companyId)
                    .orElseThrow(()-> new ResourceNotFoundException("Company not found with id : "+companyId));

            LocalDate todayDate = LocalDate.now();
            LocalDate start;
            LocalDate end;

            switch (period.toLowerCase()){
                case "today":
                    start=end=todayDate;
                    break;
                case "week":
                    start=todayDate.with(DayOfWeek.MONDAY);
                    end=todayDate.with(DayOfWeek.SUNDAY);
                    break;
                case "month":
                    start=todayDate.withDayOfMonth(1);
                    end=todayDate.withDayOfMonth(todayDate.lengthOfMonth());
                    break;
                case "quarter":
                    int currentQuarter = (todayDate.getMonthValue() - 1) / 3 + 1;
                    start = LocalDate.of(todayDate.getYear(), (currentQuarter - 1) * 3 + 1, 1);
                    end = start.plusMonths(3).minusDays(1);
                    break;
                case "year":
                    start = LocalDate.of(todayDate.getYear(), 1, 1);
                    end = LocalDate.of(todayDate.getYear(), 12, 31);
                    break;
                case "custom":
                    start = startDate;
                    end = endDate;
                    break;
                default:
                    throw new IllegalArgumentException("Invalid filterType: " + period);

            }
        List<SaleReportDto> saleReportDtoList = new ArrayList<>();

            if (saleTxnsType == null || saleTxnsType.isBlank()){
                List<SaleReportDto> saleReportOfSaleTransaction = saleRepository.findSalesReport(companyId, partyId, start, end);
                setTransactionTypeForSale(saleReportOfSaleTransaction, SaleTransactionType.SALE);

                List<SaleReportDto> saleReportOfCreditNoteTransaction = saleReturnRepository.findSalesReport(companyId,partyId,startDate,endDate);
                setTransactionTypeForSale(saleReportOfCreditNoteTransaction,SaleTransactionType.CREDIT_NOTE);

                saleReportDtoList.addAll(saleReportOfSaleTransaction);
                saleReportDtoList.addAll(saleReportOfCreditNoteTransaction);
            }
            else if(SaleTransactionType.valueOf(saleTxnsType)==SaleTransactionType.SALE) {
                saleReportDtoList = saleRepository.findSalesReport(companyId, partyId, start, end);
                setTransactionTypeForSale(saleReportDtoList,SaleTransactionType.SALE);
            } else if (SaleTransactionType.valueOf(saleTxnsType)==SaleTransactionType.CREDIT_NOTE) {
                saleReportDtoList = saleReturnRepository.findSalesReport(companyId,partyId,start,end);
                setTransactionTypeForSale(saleReportDtoList,SaleTransactionType.CREDIT_NOTE);
            }

         saleReportDtoList.sort(Comparator.comparing(SaleReportDto::getInvoceDate).reversed());

        return saleReportDtoList;
    }





    public List<PurchaseReportDto> gePurchaseReport(String period, LocalDate startDate, LocalDate endDate,
                                 Long partyId, String purchaseTxnsType,
                                 Long companyId){

        Company company = companyRepository.findById(companyId)
                .orElseThrow(()-> new ResourceNotFoundException("Company not found with id : "+companyId));

        LocalDate todayDate = LocalDate.now();
        LocalDate start;
        LocalDate end;

        switch (period.toLowerCase()){
            case "today":
                start=end=todayDate;
                break;
            case "week":
                start=todayDate.with(DayOfWeek.MONDAY);
                end=todayDate.with(DayOfWeek.SUNDAY);
                break;
            case "month":
                start=todayDate.withDayOfMonth(1);
                end=todayDate.withDayOfMonth(todayDate.lengthOfMonth());
                break;
            case "quarter":
                int currentQuarter = (todayDate.getMonthValue() - 1) / 3 + 1;
                start = LocalDate.of(todayDate.getYear(), (currentQuarter - 1) * 3 + 1, 1);
                end = start.plusMonths(3).minusDays(1);
                break;
            case "year":
                start = LocalDate.of(todayDate.getYear(), 1, 1);
                end = LocalDate.of(todayDate.getYear(), 12, 31);
                break;
            case "custom":
                start = startDate;
                end = endDate;
                break;
            default:
                throw new IllegalArgumentException("Invalid filterType: " + period);
        }

        List<PurchaseReportDto> purchaseReportDtos = new ArrayList<>();

        if (purchaseTxnsType == null || purchaseTxnsType.isBlank()){
            List<PurchaseReportDto> purchaseReports = purchaseRepository.findPurchaseReport(companyId,partyId,start,end);
           setTransactionTypeForPurchase(purchaseReports,PurchaseTransactionType.PURCHASE);


           List<PurchaseReportDto> debitNoteReports = purchaseReturnRepository.findPurchaseReport(companyId,partyId,start,end);
           setTransactionTypeForPurchase(debitNoteReports,PurchaseTransactionType.DEBIT_NOTE);

           purchaseReportDtos.addAll(purchaseReports);
           purchaseReportDtos.addAll(debitNoteReports);

        }

        else if(PurchaseTransactionType.valueOf(purchaseTxnsType)==PurchaseTransactionType.PURCHASE) {
            purchaseReportDtos = purchaseRepository.findPurchaseReport(companyId,partyId,start,end);
            setTransactionTypeForPurchase(purchaseReportDtos,PurchaseTransactionType.PURCHASE);
        } else if(PurchaseTransactionType.valueOf(purchaseTxnsType)==PurchaseTransactionType.DEBIT_NOTE) {
            purchaseReportDtos = purchaseReturnRepository.findPurchaseReport(companyId,partyId,start,end);
            setTransactionTypeForPurchase(purchaseReportDtos,PurchaseTransactionType.PURCHASE);
        }

        purchaseReportDtos.sort(Comparator.comparing(PurchaseReportDto::getBillDate).reversed());

        return purchaseReportDtos;
    }


    private void setTransactionTypeForSale(List<SaleReportDto> list, SaleTransactionType type) {
        list.forEach(dto -> dto.setSaleTransactionType(type));
    }

    private void setTransactionTypeForPurchase(List<PurchaseReportDto> list, PurchaseTransactionType type) {
        list.forEach(dto -> dto.setPurchaseTransactionType(type));
    }

}
