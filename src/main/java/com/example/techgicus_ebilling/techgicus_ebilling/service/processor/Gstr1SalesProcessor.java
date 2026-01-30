package com.example.techgicus_ebilling.techgicus_ebilling.service.processor;


import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Sale;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.SaleItem;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.TaxRate;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.gstrDto.GstrDto;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.gstrDto.InvoiceDetailDto;
import com.example.techgicus_ebilling.techgicus_ebilling.service.GstReportService;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class Gstr1SalesProcessor {

    public void process(GstrDto report,
                        List<Sale> sales,
                        String companyStateCode) {

        BigDecimal totalInvValue = BigDecimal.ZERO;
        BigDecimal totalTaxable = BigDecimal.ZERO;
        BigDecimal totalIGST = BigDecimal.ZERO;
        BigDecimal totalCGST = BigDecimal.ZERO;
        BigDecimal totalSGST = BigDecimal.ZERO;
        BigDecimal totalCess = BigDecimal.ZERO;


        for (Sale sale : sales) {
            // Add to main invoice list
            InvoiceDetailDto inv = mapSaleToInvoiceDetail(sale, companyStateCode);
            report.getInvoices().add(inv);

            totalInvValue = totalInvValue.add(BigDecimal.valueOf(sale.getTotalAmount()));
            totalTaxable = totalTaxable.add(BigDecimal.valueOf(sale.getTotalAmountWithoutTax()));


            // Item level processing
            for (SaleItem item : sale.getSaleItem()) {
                BigDecimal taxAmt = BigDecimal.valueOf(item.getTaxAmount());

                // Tax split logic
                boolean intraState = false;
                if (sale.getStateOfSupply() !=null){
                    intraState = companyStateCode.equals(sale.getStateOfSupply().getCode());
                }
                else{
                    intraState = true;
                }
                BigDecimal iamt = BigDecimal.ZERO;
                BigDecimal camt = BigDecimal.ZERO;
                BigDecimal samt = BigDecimal.ZERO;
                if (intraState) {
                    camt = taxAmt.divide(BigDecimal.valueOf(2), 2, RoundingMode.HALF_UP);
                    samt = taxAmt.subtract(camt);
                } else {
                    iamt = taxAmt;
                }

                totalIGST = totalIGST.add(iamt);
                totalCGST = totalCGST.add(camt);
                totalSGST = totalSGST.add(samt);
            }
        }

        // Set totals
        report.setTotalInvoiceValue(totalInvValue);
        report.setTotalTaxableValue(totalTaxable);
        report.setTotalIntegratedTax(totalIGST);
        report.setTotalCentralTax(totalCGST);
        report.setTotalStateUtTax(totalSGST);
        report.setTotalCess(totalCess);
    }


    private InvoiceDetailDto mapSaleToInvoiceDetail(Sale sale, String companyStateCode) {
        InvoiceDetailDto dto = new InvoiceDetailDto();
        dto.setPartyName(sale.getParty() != null ? sale.getParty().getName() : "");
        dto.setGstinUin(sale.getParty() != null ? sale.getParty().getGstin() : "");
        dto.setTransactionType("SALE");
        dto.setInvoiceNo(sale.getInvoiceNumber());
        dto.setInvoiceDate(sale.getInvoceDate());
        dto.setInvoiceValue(BigDecimal.valueOf(sale.getTotalAmount()));
        dto.setTaxableValue(BigDecimal.valueOf(sale.getTotalAmountWithoutTax()));
        dto.setReverseCharge("N");

        BigDecimal tax = BigDecimal.valueOf(sale.getTotalTaxAmount());
        boolean intra = false;
        if (sale.getStateOfSupply() == null) {
            intra = true;  // Sale {} has null stateOfSupply – assuming intra-state
        } else {
            intra = companyStateCode.equals(sale.getStateOfSupply().getCode());
        }
        if (intra) {
            dto.setIntegratedTaxAmount(BigDecimal.ZERO);
            dto.setCentralTaxAmount(tax.divide(BigDecimal.valueOf(2), 2, RoundingMode.HALF_UP));
            dto.setStateUtTaxAmount(tax.subtract(dto.getCentralTaxAmount()));
        } else {
            dto.setIntegratedTaxAmount(tax);
            dto.setCentralTaxAmount(BigDecimal.ZERO);
            dto.setStateUtTaxAmount(BigDecimal.ZERO);
        }
        dto.setCessAmount(BigDecimal.ZERO);
        if (sale.getStateOfSupply() != null) {
            dto.setPlaceOfSupply(sale.getStateOfSupply().getDisplayName());
        }
        else {
            dto.setPlaceOfSupply(sale.getCompany().getState().getDisplayName());
        }

        if (!sale.getSaleItem().isEmpty()) {
            dto.setRate(TaxRate.getRateFromTaxRate(sale.getSaleItem().get(0).getTaxRate()));
        }

        dto.setCessRate(BigDecimal.ZERO);

        return dto;
    }


}
