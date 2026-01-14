package com.example.techgicus_ebilling.techgicus_ebilling.service;

import com.example.techgicus_ebilling.techgicus_ebilling.dto.taxDto.ItemTaxSummaryResponse;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.taxDto.TaxComponentResponse;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.saleDto.SaleItemResponse;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.taxDto.TaxableItem;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class TaxCalculationService {

    public List<ItemTaxSummaryResponse> calculateTaxSummary(List<? extends TaxableItem> items) {
        List<ItemTaxSummaryResponse> taxSummaryList = new ArrayList<>();

        double firstTaxRate = items.get(0).getTaxRate().getRate();
        String firstHsnCode = items.get(0).getItemHsnCode();



        boolean isTaxRateEqual = items.stream()
                .allMatch(item -> Double.compare(item.getTaxRate().getRate(), firstTaxRate) == 0);

        boolean isTaxRateAndHsnEqual = items.stream()
                .allMatch(item ->
                        Double.compare(item.getTaxRate().getRate(), firstTaxRate) == 0
                                && Objects.equals(item.getItemHsnCode(), firstHsnCode)
                );

        if (isTaxRateAndHsnEqual){

            double taxAmount = items.stream()
                    .mapToDouble(TaxableItem::getTaxAmount)
                    .sum();

            double totalAmount = items.stream()
                    .mapToDouble(TaxableItem::getTotalAmount)
                    .sum();

            ItemTaxSummaryResponse summary = new ItemTaxSummaryResponse();
            //summary.setItemName(item.getItemName());
             summary.setHsnCode(items.get(0).getItemHsnCode());

            double taxPaybleAmount = totalAmount-taxAmount;

            double roundTaxPaybleAmount = Math.floor(taxPaybleAmount*100)/100;

            summary.setTaxableAmount(roundTaxPaybleAmount); // base amount for tax

            double halfTaxRate = firstTaxRate/2.0;
            System.out.println("Tax amount : "+taxAmount);
            double halfTaxAmount = taxAmount/2.0;
            System.out.println("half Tax amount : "+halfTaxAmount);


            double roundHalfTaxAmount = Math.floor(halfTaxAmount*100)/100;
            System.out.println("round Tax amount : "+roundHalfTaxAmount);


            // Calculate CGST
            TaxComponentResponse cgst = new TaxComponentResponse();

            cgst.setRate(halfTaxRate);
            cgst.setAmount(roundHalfTaxAmount);
            summary.setCgst(cgst);

            // Calculate SGST
            TaxComponentResponse sgst = new TaxComponentResponse();
            sgst.setRate(halfTaxRate);
            sgst.setAmount(roundHalfTaxAmount);
            summary.setSgst(sgst);

            // Total Tax Amount
            summary.setTotalTaxAmount(taxAmount);

            taxSummaryList.add(summary);

            return  taxSummaryList;
        }


        for (TaxableItem item : items) {

            double taxAmount = item.getTaxAmount();

            ItemTaxSummaryResponse summary = new ItemTaxSummaryResponse();
            //summary.setItemName(item.getItemName());
             summary.setHsnCode(item.getItemHsnCode());

            double taxPaybleAmount = item.getTotalAmount()-taxAmount;

            double roundTaxPaybleAmount = Math.floor(taxPaybleAmount*100)/100;

            summary.setTaxableAmount(roundTaxPaybleAmount); // base amount for tax

            double halfTaxRate = item.getTaxRate().getRate()/2.0;
            System.out.println("Tax amount : "+taxAmount);
            double halfTaxAmount = taxAmount/2.0;
            System.out.println("half Tax amount : "+halfTaxAmount);


            double roundHalfTaxAmount = Math.floor(halfTaxAmount*100)/100;
            System.out.println("round Tax amount : "+roundHalfTaxAmount);


            // Calculate CGST
            TaxComponentResponse cgst = new TaxComponentResponse();

            cgst.setRate(halfTaxRate);
            cgst.setAmount(roundHalfTaxAmount);
            summary.setCgst(cgst);

            // Calculate SGST
            TaxComponentResponse sgst = new TaxComponentResponse();
            sgst.setRate(halfTaxRate);
            sgst.setAmount(roundHalfTaxAmount);
            summary.setSgst(sgst);

            // Total Tax Amount
            summary.setTotalTaxAmount(taxAmount);

            taxSummaryList.add(summary);
        }

        return taxSummaryList;
    }





}

