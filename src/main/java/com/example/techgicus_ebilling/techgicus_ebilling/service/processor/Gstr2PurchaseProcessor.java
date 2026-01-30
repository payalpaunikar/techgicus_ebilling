package com.example.techgicus_ebilling.techgicus_ebilling.service.processor;

import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Purchase;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.PurchaseItem;


import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.TaxRate;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.gstrDto.GstrDto;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.gstrDto.InvoiceDetailDto;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class Gstr2PurchaseProcessor {
    
    public void process(GstrDto report, List<Purchase> purchases,
                        String companyStateCode){


        BigDecimal totalInvValue = BigDecimal.ZERO;
        BigDecimal totalTaxable = BigDecimal.ZERO;
        BigDecimal totalIGST = BigDecimal.ZERO;
        BigDecimal totalCGST = BigDecimal.ZERO;
        BigDecimal totalSGST = BigDecimal.ZERO;
        BigDecimal totalCess = BigDecimal.ZERO;

        for (Purchase purchase : purchases) {
            // Add to main invoice list
            InvoiceDetailDto inv = mapPurchaseToDto(purchase, companyStateCode);
            report.getInvoices().add(inv);

            totalInvValue = totalInvValue.add(BigDecimal.valueOf(purchase.getTotalAmount()));
            totalTaxable = totalTaxable.add(BigDecimal.valueOf(purchase.getTotalAmountWithoutTax()));


            // Item level processing
            for (PurchaseItem item : purchase.getPurchaseItems()) {
                BigDecimal taxAmt = BigDecimal.valueOf(item.getTotalTaxAmount());

                // Tax split logic
                boolean intraState = false;
                if (purchase.getStateOfSupply() !=null){
                    intraState = companyStateCode.equals(purchase.getStateOfSupply().getCode());
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
    
    
    private InvoiceDetailDto mapPurchaseToDto(Purchase purchase,String companyStateCode){
        InvoiceDetailDto dto = new InvoiceDetailDto();
        dto.setPartyName(purchase.getParty() != null ? purchase.getParty().getName() : "");
        dto.setGstinUin(purchase.getParty() != null ? purchase.getParty().getGstin() : "");
        dto.setTransactionType("Purchase");
        dto.setInvoiceNo(purchase.getBillNumber());
        dto.setInvoiceDate(purchase.getBillDate());
        dto.setInvoiceValue(BigDecimal.valueOf(purchase.getTotalAmount()));
        dto.setTaxableValue(BigDecimal.valueOf(purchase.getTotalAmountWithoutTax()));
        dto.setReverseCharge("N");

        BigDecimal tax = BigDecimal.valueOf(purchase.getTotalTaxAmount());
        boolean intra = false;
        if (purchase.getStateOfSupply() == null) {
            intra = true;  // purchase {} has null stateOfSupply – assuming intra-state
        } else {
            intra = companyStateCode.equals(purchase.getStateOfSupply().getCode());
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
        if (purchase.getStateOfSupply() != null) {
            dto.setPlaceOfSupply(purchase.getStateOfSupply().getDisplayName());
        }
        else {
            dto.setPlaceOfSupply(purchase.getCompany().getState().getDisplayName());
        }

        if (!purchase.getPurchaseItems().isEmpty()) {
            dto.setRate(TaxRate.getRateFromTaxRate(purchase.getPurchaseItems().get(0).getTaxRate()));
        }

        dto.setCessRate(BigDecimal.ZERO);

        return dto;
    
    }
}
