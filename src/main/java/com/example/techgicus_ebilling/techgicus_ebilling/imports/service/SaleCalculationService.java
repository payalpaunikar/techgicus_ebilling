package com.example.techgicus_ebilling.techgicus_ebilling.imports.service;


import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Sale;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.SaleItem;
import org.springframework.stereotype.Service;

@Service
public class SaleCalculationService {

    public void recalculateSaleTotals(Sale sale) {

        double totalWithoutTax = 0;
        double totalTax = 0;

        for (SaleItem item : sale.getSaleItem()) {
            totalWithoutTax += item.getPricePerUnit() * item.getQuantity();
            totalTax += item.getTaxAmount();
        }

        sale.setTotalAmountWithoutTax(totalWithoutTax);
        sale.setTotalTaxAmount(totalTax);
        sale.setTotalAmount(totalWithoutTax + totalTax);

        double received = sale.getReceivedAmount() != null ? sale.getReceivedAmount() : 0;
        sale.setBalance(sale.getTotalAmount() - received);

        sale.setPaid(sale.getBalance() <= 0);
        sale.setOverdue(!sale.getPaid());
    }
}

