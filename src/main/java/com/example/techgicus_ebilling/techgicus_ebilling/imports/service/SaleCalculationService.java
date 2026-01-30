package com.example.techgicus_ebilling.techgicus_ebilling.imports.service;


import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Sale;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.SaleItem;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.SaleReturn;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.SaleReturnItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SaleCalculationService {


    private final static Logger log = LoggerFactory.getLogger(SaleCalculationService.class);

    public Sale recalculateSaleTotals(Sale sale) {

        log.info("Recalculating sale totals for invoice {}", sale.getInvoiceNumber());

        double totalWithoutTax = 0.0;
        double totalTax = 0.0;

        for (SaleItem item : sale.getSaleItem()) {
            totalWithoutTax += item.getPricePerUnit() * item.getQuantity();
            totalTax += item.getTaxAmount();
        }

        double totalAmount = totalWithoutTax + totalTax;
        double received = sale.getReceivedAmount() != null ? sale.getReceivedAmount() : 0.0;

        sale.setTotalAmountWithoutTax(totalWithoutTax);
        sale.setTotalTaxAmount(totalTax);
        sale.setTotalAmount(totalAmount);

        double balance = totalAmount - received;
        sale.setBalance(balance);

        boolean paid = balance <= 0.01; // tolerance
        sale.setPaid(paid);
        sale.setOverdue(!paid);

        log.info(
                "Sale totals updated → withoutTax={}, tax={}, total={}, received={}, balance={}",
                totalWithoutTax, totalTax, totalAmount, received, balance
        );

        return sale;
    }



    public SaleReturn recalculateSaleTotals(SaleReturn sale) {

        double saleReceivedAmount = sale.getPaidAmount();
        double saleTotalAmount = sale.getTotalAmount();
        double saleBalanceAmount = sale.getBalanceAmount();

        double totalWithoutTax = 0;
        double totalTax = 0;
        double totalQuantity = 0.0;

        for (SaleReturnItem item : sale.getSaleReturnItems()) {

            totalWithoutTax += item.getRatePerUnit() * item.getQuantity();
            totalTax += item.getTotalTaxAmount();
            totalQuantity = totalQuantity + item.getQuantity();
        }

        double totalAmount = totalWithoutTax+totalTax;



        if (totalAmount == saleTotalAmount) {



          //  sale.setTotalAmountWithoutTax(totalWithoutTax);
            sale.setTotalTaxAmount(totalTax);
            // sale.setTotalAmount(totalWithoutTax + totalTax);
//            sale.setPaid(sale.getBalance() <= 0);
//            sale.setOverdue(!sale.getPaid());

            sale.setTotalQuantity(totalQuantity);

        }
        else {

          //  sale.setTotalAmountWithoutTax(totalWithoutTax);
            sale.setTotalTaxAmount(totalTax);
            sale.setTotalAmount(totalWithoutTax + totalTax);
            double received = sale.getPaidAmount() != null ? sale.getPaidAmount() : 0;
            sale.setBalanceAmount(sale.getTotalAmount() - received);
            sale.setTotalQuantity(totalQuantity);

//            sale.setPaid(sale.getBalance() <= 0);
//            sale.setOverdue(!sale.getPaid());
        }

        return sale;
    }
}

