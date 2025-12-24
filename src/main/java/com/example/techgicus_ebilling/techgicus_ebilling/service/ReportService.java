package com.example.techgicus_ebilling.techgicus_ebilling.service;


import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.*;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.PurchaseTransactionType;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.SaleTransactionType;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.reportDto.*;
import com.example.techgicus_ebilling.techgicus_ebilling.exception.ResourceNotFoundException;
import com.example.techgicus_ebilling.techgicus_ebilling.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;

@Service
public class ReportService {


        private SaleRepository saleRepository;
        private PurchaseRepository purchaseRepository;
        private SaleItemRepository saleItemRepository;
        private PurchaseItemRepository purchaseItemRepository;
        private CompanyRepository companyRepository;
        private SaleReturnRepository saleReturnRepository;
        private PurchaseReturnRepository purchaseReturnRepository;
        private PartyLedgerRepository partyLedgerRepository;
        private PartyRepository partyRepository;
        private StockTransactionRepository stockTransactionRepository;
        private ExpenseRepository expenseRepository;

        private static final Logger log = LoggerFactory.getLogger(ReportService.class);

    @Autowired
    public ReportService(SaleRepository saleRepository, PurchaseRepository purchaseRepository, SaleItemRepository saleItemRepository, PurchaseItemRepository purchaseItemRepository, CompanyRepository companyRepository, SaleReturnRepository saleReturnRepository, PurchaseReturnRepository purchaseReturnRepository, PartyLedgerRepository partyLedgerRepository, PartyRepository partyRepository, StockTransactionRepository stockTransactionRepository, ExpenseRepository expenseRepository) {
        this.saleRepository = saleRepository;
        this.purchaseRepository = purchaseRepository;
        this.saleItemRepository = saleItemRepository;
        this.purchaseItemRepository = purchaseItemRepository;
        this.companyRepository = companyRepository;
        this.saleReturnRepository = saleReturnRepository;
        this.purchaseReturnRepository = purchaseReturnRepository;
        this.partyLedgerRepository = partyLedgerRepository;
        this.partyRepository = partyRepository;
        this.stockTransactionRepository = stockTransactionRepository;
        this.expenseRepository = expenseRepository;
    }

    public List<SaleReportDto> getSaleReport(String period, LocalDate startDate, LocalDate endDate,
                                             Long partyId, String saleTxnsType,
                                             Long companyId){

            Company company = companyRepository.findById(companyId)
                    .orElseThrow(()-> new ResourceNotFoundException("Company not found with id : "+companyId));

        Map<String,LocalDate> startDateAndEndDate = getStartDateAndEndDate(period,startDate,endDate);

        LocalDate start = startDateAndEndDate.get("startDate");
        LocalDate end = startDateAndEndDate.get("endDate");

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





    public List<PurchaseReportDto> gePurchaseReport(String period, LocalDate fromDate, LocalDate toDate,
                                 Long partyId, String purchaseTxnsType,
                                 Long companyId){

        Company company = companyRepository.findById(companyId)
                .orElseThrow(()-> new ResourceNotFoundException("Company not found with id : "+companyId));

        Map<String,LocalDate> startDateAndEndDate = getStartDateAndEndDate(period,fromDate,toDate);

        LocalDate start = startDateAndEndDate.get("startDate");
        LocalDate end = startDateAndEndDate.get("endDate");

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



    public List<PartyStatement> getPartyStatementList(String period, LocalDate fromDate, LocalDate toDate,
                                                                Long partyId){

        Map<String,LocalDate> startDateAndEndDate = getStartDateAndEndDate(period,fromDate,toDate);

        LocalDate start = startDateAndEndDate.get("startDate");
        LocalDate end = startDateAndEndDate.get("endDate");


       List<PartyLedger> partyLedgerList = partyLedgerRepository.findByPartyPartyIdAndDateBetweenOrderByDateAsc(partyId,start,end);

        List<PartyStatement> partyStatementList = convertPartyLeaderListIntoPartyStatementList(partyLedgerList);

        return partyStatementList;
    }


    public List<PartyReport> getAllPartyReports(Long companyId){

     List<PartyReportProjection> projectionList = partyRepository.getAllPartyReport(companyId);

     List<PartyReport> partyReportList = new ArrayList<>();

     for (PartyReportProjection projection : projectionList){

         Double receivable = 0.0;
         Double payable = 0.0;

         // set receivable and payable amount
         if(projection.getTotalCredit() > projection.getTotalDebit()){
             payable = projection.getTotalCredit() - projection.getTotalDebit();
         }
         else{
             receivable = projection.getTotalDebit() - projection.getTotalCredit();
         }


         PartyReport partyReport = new PartyReport();
         partyReport = convertPartReportProjectIntoPartyReport(projection,receivable,payable);

         partyReportList.add(partyReport);
     }

     return partyReportList;
    }



    public ProfitAndLossReport generateProfitAndLoss(Long companyId, String period, LocalDate fromDate, LocalDate toDate){

       Map<String,LocalDate> startDateAndEndDate = getStartDateAndEndDate(period,fromDate,toDate);

       LocalDate startDate = startDateAndEndDate.get("startDate");
       LocalDate endDate = startDateAndEndDate.get("endDate");

        Double sale = saleRepository.findRevenueByDate(companyId,startDate,endDate);
        Double creditNote = saleReturnRepository.sumCreditNoteByDate(companyId,startDate,endDate);
        Double saleFA = 0.0;

        Double purchase = purchaseRepository.sumPurchaseByDate(companyId,startDate,endDate);
        Double debitNote = purchaseReturnRepository.sumDebitNoteByDate(companyId,startDate,endDate);
        Double purchaseFa = 0.0;


        Double paymentOutDiscount = 0.0;
        Double paymentInDiscount = 0.0;


        Double otherDirectExpenses = 0.0;

        Double saleTotalTaxAmount = saleRepository.sumTaxAmountByDate(companyId,startDate,endDate);
        Double saleReturnTotalTaxAmount = saleReturnRepository.sumTaxAmountByDate(companyId,startDate,endDate);
        Double gstPayable = saleTotalTaxAmount - saleReturnTotalTaxAmount;
        gstPayable = Math.round(gstPayable * 100.0) / 100.0;

        Double tcsPayable = 0.0;
        Double tdsPayable = 0.0;


        Double purchaseTotalTaxAmount = purchaseRepository.sumTaxAmountByDate(companyId,startDate,endDate);
        Double purchaseReturnTotalTaxAmount = purchaseReturnRepository.sumTaxAmountByDate(companyId,startDate,endDate);
        Double gstReceivable =purchaseTotalTaxAmount - purchaseReturnTotalTaxAmount;
        Double tcsReceivable = 0.0;
        Double tdsReceivable = 0.0;

       Double openingStock = 0.0 ;
       Double closingStock = stockTransactionRepository.sumClosingStock(companyId,startDate,endDate);
       Double openingStockFA = 0.0;
       Double closingStockFA = 0.0;

       Double grossProfitAndLoss = 0.0;
       Double otherIncome = 0.0;

       Double otherExpense = expenseRepository.sumOtherExpense(companyId,startDate,endDate);
       Double loanInterestExpense = 0.0;
       Double loanProcessingFeeExpense = 0.0;
       Double loanChargeExpense = 0.0;


       Double netProfitAndLoss = 0.0;


     ProfitAndLossReport profitAndLossReport =  setProfitAndLossReportFields(sale,creditNote,saleFA,
               purchase,debitNote,purchaseFa,
               paymentOutDiscount,paymentInDiscount,
               otherDirectExpenses,
               gstPayable,tcsPayable,tdsPayable,
               gstReceivable,tcsReceivable,tdsReceivable,
               openingStock,closingStock,openingStockFA,closingStockFA,
             grossProfitAndLoss,otherIncome,
               otherExpense,loanInterestExpense,loanProcessingFeeExpense,loanChargeExpense,
               netProfitAndLoss);


        return profitAndLossReport;
    }


    public List<BillWiseProfitAndLossSummary> generatedBillWiseProfitAndLossReport(String period, Long partyId, Long companyId, LocalDate fromDate,
                                                                                   LocalDate toDate){

        Map<String,LocalDate> startDateAndEndDate = getStartDateAndEndDate(period,fromDate,toDate);

        LocalDate startDate = startDateAndEndDate.get("startDate");
        LocalDate endDate = startDateAndEndDate.get("endDate");


        List<Sale> saleList = saleRepository.findAllByFilter(companyId,partyId,startDate,endDate);


        //Convert saleList into  BillWiseProfitAndLossList
        List<BillWiseProfitAndLossSummary> billWiseProfitAndLossSummaries = saleList.stream()
                .map(sale -> {
                    BillWiseProfitAndLossSummary billWiseProfitAndLossSummary = convertSaleIntoBillWiseProfitAndLoss(sale);
                    return billWiseProfitAndLossSummary;
                }).toList();

        return billWiseProfitAndLossSummaries;
    }


    public BillWiseProfitAndLossDetail getBillWiseProfitAndLossDetailBySaleId(Long saleId){
        Sale sale = saleRepository.findById(saleId)
                .orElseThrow(()-> new ResourceNotFoundException("Sale not fount with id : "+saleId));

     BillWiseProfitAndLossDetail detail =  convertSaleIntoBillWiseProfitAndLossDetail(sale);

     return detail;
    }


    private BillWiseProfitAndLossDetail convertSaleIntoBillWiseProfitAndLossDetail(Sale sale){
        BillWiseProfitAndLossDetail  billWiseProfitAndLossDetail = new BillWiseProfitAndLossDetail();
        billWiseProfitAndLossDetail.setSaleAmount(sale.getTotalAmountWithoutTax());
        billWiseProfitAndLossDetail.setTaxPayable(sale.getTotalTaxAmount());
        billWiseProfitAndLossDetail.setTdsPayable(0.0);

        Map<String,Object> objectMap = setBillWiseProfitAndLossItemListFieldsAndCalculateTotalCost(sale.getSaleItem());

        // Cast te object into list.
        @SuppressWarnings("unchecked")
        List<BillWiseProfitAndLossItem> items =
                (List<BillWiseProfitAndLossItem>) objectMap.get("BillWiseProfitLossItemList");

        Double totalCost = (Double) objectMap.get("totalCost");

        billWiseProfitAndLossDetail.setItems(items);
        billWiseProfitAndLossDetail.setTotalCost(totalCost);

        Double total = billWiseProfitAndLossDetail.getSaleAmount()+ billWiseProfitAndLossDetail.getTotalCost()
                + billWiseProfitAndLossDetail.getTaxPayable() - billWiseProfitAndLossDetail.getTdsPayable();

        billWiseProfitAndLossDetail.setTotal(total);


        return billWiseProfitAndLossDetail;

    }


    private BillWiseProfitAndLossSummary convertSaleIntoBillWiseProfitAndLoss(Sale sale){

        Double saleAmount = sale.getTotalAmountWithoutTax();

        List<SaleItem> saleItemList = sale.getSaleItem();

        // calculated the total cost
        Double totalCost = calculatedTotalCost(saleItemList);

        Double taxPayable = sale.getTotalTaxAmount();

        Double tdsReceivable = 0.0;

        Double profitAndLossAmount = saleAmount - totalCost -taxPayable + tdsReceivable;

        BillWiseProfitAndLossSummary billWiseProfitAndLossSummary = new BillWiseProfitAndLossSummary();
        billWiseProfitAndLossSummary.setSaleId(sale.getSaleId());
        billWiseProfitAndLossSummary.setDate(sale.getInvoceDate());
        billWiseProfitAndLossSummary.setInvoiceNo(sale.getInvoiceNumber());
        billWiseProfitAndLossSummary.setPartyName(sale.getParty().getName());
        billWiseProfitAndLossSummary.setTotalSaleAmount(saleAmount);
        billWiseProfitAndLossSummary.setProfitAndLoss(profitAndLossAmount);

        return billWiseProfitAndLossSummary;
    }


    private Double calculatedTotalCost(List<SaleItem> saleItemList){
        Double totalCost = saleItemList.stream()
                .mapToDouble(saleItem -> {
                    Double purchasePrice = saleItem.getItem().getPurchasePrice();
                    if (purchasePrice == null) purchasePrice =0.0;

                    return saleItem.getQuantity() * purchasePrice;

                }).sum();

        return totalCost;
    }


    private Map<String,Object> setBillWiseProfitAndLossItemListFieldsAndCalculateTotalCost(List<SaleItem> saleItemList){

        List<BillWiseProfitAndLossItem> billWiseProfitAndLossItemList = new ArrayList<>();
        Double totalCost = 0.0;
       for (SaleItem saleItem :saleItemList){

           // get Item from Sale Item
           Item item = saleItem.getItem();
           Double purchasePrice = item.getPurchasePrice();
           if (purchasePrice == null) purchasePrice =0.0;

           Double saleItemQuantity = saleItem.getQuantity();
           if (saleItemQuantity == null) saleItemQuantity = 0.0;

           BillWiseProfitAndLossItem billWiseProfitAndLossItem = new BillWiseProfitAndLossItem();
           billWiseProfitAndLossItem.setItemName(item.getItemName());
           billWiseProfitAndLossItem.setQuantity(saleItem.getQuantity());
           billWiseProfitAndLossItem.setPurchasePrice(purchasePrice);
           billWiseProfitAndLossItem.setTotalCost(saleItemQuantity*purchasePrice);

           totalCost = totalCost + billWiseProfitAndLossItem.getTotalCost();

           billWiseProfitAndLossItemList.add(billWiseProfitAndLossItem);
       }

       log.info("total cost "+totalCost);

       Map<String,Object> objectMap = new HashMap<>();
       objectMap.put("BillWiseProfitLossItemList",billWiseProfitAndLossItemList);
       objectMap.put("totalCost",totalCost);

       return objectMap;
    }


    private ProfitAndLossReport setProfitAndLossReportFields(Double sale, Double creditNote, Double saleFa,
                                                             Double purchase, Double debitNote, Double purchaseFa,
                                                             Double paymentOutDiscount, Double paymentInDiscount,
                                                             Double otherDirectExpenses,
                                                             Double gstPayable, Double tcsPayable, Double tdsPayable,
                                                             Double gstReceivable, Double tcsReceivable, Double tdsReceivable,
                                                             Double openingStock, Double closingStock, Double openingStockFA, Double closingStockFA,
                                                             Double grossProfitAndLoss, Double otherIncome,
                                                             Double otherExpense, Double loanInterestExpense, Double loanProcessingFeeExpense, Double loanChargeExpense,
                                                             Double netProfitAndLoss){

        ProfitAndLossReport profitAndLossReport = new ProfitAndLossReport();
        profitAndLossReport.setSale(sale);
        profitAndLossReport.setCreditNote(creditNote);
        profitAndLossReport.setSaleFA(saleFa);
        profitAndLossReport.setPurchase(purchase);
        profitAndLossReport.setDebitNote(debitNote);
        profitAndLossReport.setPurchaseFA(purchaseFa);
        profitAndLossReport.setPaymentOutDiscount(paymentOutDiscount);
        profitAndLossReport.setPaymentInDiscount(paymentInDiscount);
        profitAndLossReport.setOtherDirectExpenses(otherDirectExpenses);
        profitAndLossReport.setGstPayable(gstPayable);
        profitAndLossReport.setTcsPayable(tcsPayable);
        profitAndLossReport.setTdsPayable(tdsPayable);
        profitAndLossReport.setGstReceivable(gstReceivable);
        profitAndLossReport.setTcsReceivable(tcsReceivable);
        profitAndLossReport.setTdsReceivable(tdsReceivable);
        profitAndLossReport.setOpeningStock(openingStock);
        profitAndLossReport.setClosingStock(closingStock);
        profitAndLossReport.setOpeningStockFA(openingStockFA);
        profitAndLossReport.setClosingStockFA(closingStockFA);
        profitAndLossReport.setGrossProfitAndLoss(grossProfitAndLoss);
        profitAndLossReport.setOtherIncome(otherIncome);
        profitAndLossReport.setOtherExpense(otherExpense);
        profitAndLossReport.setLoanInterestExpense(loanInterestExpense);
        profitAndLossReport.setLoanProcessingFeeExpense(loanProcessingFeeExpense);
        profitAndLossReport.setLoanChargeExpense(loanChargeExpense);
        profitAndLossReport.setNetProfitAndLoss(netProfitAndLoss);

        return profitAndLossReport;
    }


    private PartyReport convertPartReportProjectIntoPartyReport(PartyReportProjection projection,Double receivable,
    Double payable){

        PartyReport partyReport = new PartyReport();
        partyReport.setPartyId(projection.getPartyId());
        partyReport.setName(projection.getPartyName());
        partyReport.setGstin(projection.getGstin());
        partyReport.setPhoneNo(projection.getPhoneNo());
        partyReport.setGstType(projection.getGstType());
        partyReport.setState(projection.getState());
        partyReport.setEmailId(projection.getEmailId());
        partyReport.setBillingAddress(projection.getbillingAddress());
        partyReport.setShipingAddress(partyReport.getShipingAddress());
        partyReport.setReceivableBalance(receivable);
        partyReport.setPayableBalance(payable);

        return partyReport;
    }


    private void setTransactionTypeForSale(List<SaleReportDto> list, SaleTransactionType type) {
        list.forEach(dto -> dto.setSaleTransactionType(type));
    }

    private void setTransactionTypeForPurchase(List<PurchaseReportDto> list, PurchaseTransactionType type) {
        list.forEach(dto -> dto.setPurchaseTransactionType(type));
    }


    private PartyStatement convertPartyLeaderIntoPartyTransactionResponse(PartyLedger partyLedger){
       PartyStatement partyStatement = new PartyStatement();
       partyStatement.setId(partyLedger.getLedgerId());
       partyStatement.setTransactionDate(partyLedger.getDate());
       partyStatement.setReferenceId(partyLedger.getReferenceId());
       partyStatement.setReferenceNumber(partyLedger.getReferenceNo());
       partyStatement.setCreditAmount(partyLedger.getCreditAmount());
       partyStatement.setDebitAmount(partyLedger.getDebitAmount());
       partyStatement.setRunningBalance(partyLedger.getRunningBalance());
       partyStatement.setTransactionType(partyLedger.getType());

       return partyStatement;
    }


    public List<PartyStatement> convertPartyLeaderListIntoPartyStatementList(List<PartyLedger> partyLedgerList){

        List<PartyStatement> partyStatementList = partyLedgerList.stream()
                .map(partyLedger -> convertPartyLeaderIntoPartyTransactionResponse(partyLedger))
                .toList();

        return partyStatementList;
    }

    private Map<String,LocalDate> getStartDateAndEndDate(String period,LocalDate startDate,LocalDate endDate){
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

        Map startDateAndEndDateMap = new HashMap<>();

        startDateAndEndDateMap.put("startDate",start);
        startDateAndEndDateMap.put("endDate",end);

        return startDateAndEndDateMap;
    }
}
