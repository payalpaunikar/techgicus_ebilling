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

        List<ItemTaxSummaryResponse> summaries = new ArrayList<>();

        if (items == null || items.isEmpty()) {
            return summaries;
        }

        // 🔹 Group items by GST percentage
        Map<Double, List<TaxableItem>> groupedByTaxRate =
                items.stream()
                        .collect(Collectors.groupingBy(
                                item -> item.getTaxRate().getRate()
                        ));

        // 🔹 Process each GST group separately
        for (Map.Entry<Double, List<TaxableItem>> entry : groupedByTaxRate.entrySet()) {

            double gstRate = entry.getKey();                // e.g. 18
            List<TaxableItem> groupItems = entry.getValue();

            // ✅ Total amount INCLUDING GST
            double totalAmountInclusive = groupItems.stream()
                    .mapToDouble(TaxableItem::getTotalAmount)
                    .sum();

            double taxAmount = groupItems.stream()
                    .mapToDouble(TaxableItem::getTaxAmount)
                    .sum();

//            // ✅ Correct taxable amount calculation (GST inclusive formula)
//            double taxableAmount =
//                    totalAmountInclusive / (1 + gstRate / 100.0);

            double taxableAmount = totalAmountInclusive - taxAmount;

//            // ✅ Total GST amount
//            double totalTaxAmount =
//                    totalAmountInclusive - taxableAmount;

            // CGST & SGST
            double halfTaxRate = gstRate / 2.0;
            double halfTaxAmount = taxAmount / 2.0;

            ItemTaxSummaryResponse summary = new ItemTaxSummaryResponse();
            //summary.setHsnCode(null); // HSN intentionally removed
            summary.setTaxableAmount(round2(taxableAmount));
            summary.setTotalTaxAmount(round2(taxAmount));

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

            summaries.add(summary);
        }

        return summaries;
    }

    // 🔹 Correct rounding for tax calculations
    private double round2(double value) {
        return BigDecimal.valueOf(value)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }
}



