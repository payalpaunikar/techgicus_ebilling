package com.example.techgicus_ebilling.techgicus_ebilling.service;

import com.example.techgicus_ebilling.techgicus_ebilling.dto.taxDto.ItemTaxSummaryResponse;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.taxDto.TaxComponentResponse;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.saleDto.SaleItemResponse;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.taxDto.TaxableItem;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TaxCalculationService {

    public List<ItemTaxSummaryResponse> calculateTaxSummary(
            List<? extends TaxableItem> items) {

        List<ItemTaxSummaryResponse> taxSummaryList = new ArrayList<>();

        if (items == null || items.isEmpty()) {
            return taxSummaryList;
        }

        // 🔹 Group by HSN + Tax Rate
        Map<String, List<TaxableItem>> groupedItems =
                items.stream()
                        .collect(Collectors.groupingBy(
                                item -> item.getItemHsnCode() + "_" + item.getTaxRate().getRate()
                        ));

        // 🔹 Process each group separately
        for (List<TaxableItem> group : groupedItems.values()) {

            TaxableItem firstItem = group.get(0);

            String hsnCode = firstItem.getItemHsnCode();
            double taxRate = firstItem.getTaxRate().getRate();

            double totalTaxAmount = group.stream()
                    .mapToDouble(TaxableItem::getTaxAmount)
                    .sum();

            double totalAmount = group.stream()
                    .mapToDouble(TaxableItem::getTotalAmount)
                    .sum();

            double taxableAmount = totalAmount - totalTaxAmount;

            ItemTaxSummaryResponse summary = new ItemTaxSummaryResponse();
            summary.setHsnCode(hsnCode);
            summary.setTaxableAmount(round2(taxableAmount));

            double halfTaxRate = taxRate / 2.0;
            double halfTaxAmount = totalTaxAmount / 2.0;

            // CGST
            TaxComponentResponse cgst = new TaxComponentResponse();
            cgst.setRate(halfTaxRate);
            cgst.setAmount(round2(halfTaxAmount));
            summary.setCgst(cgst);

            // SGST
            TaxComponentResponse sgst = new TaxComponentResponse();
            sgst.setRate(halfTaxRate);
            sgst.setAmount(round2(halfTaxAmount));
            summary.setSgst(sgst);

            summary.setTotalTaxAmount(round2(totalTaxAmount));

            taxSummaryList.add(summary);
        }

        return taxSummaryList;
    }

    // 🔹 Proper rounding to 2 decimal places
    private double round2(double value) {
        return BigDecimal.valueOf(value)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }
}


