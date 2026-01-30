package com.example.techgicus_ebilling.techgicus_ebilling.imports.mapper;

import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Sale;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.SalePayment;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;


@Component
public class SalePaymentEntityMapper {

    public SalePayment toEntity(Sale sale,SalePayment salePayment){
        salePayment.setPaymentDate(sale.getInvoceDate());
        salePayment.setAmountPaid(sale.getReceivedAmount());
        salePayment.setPaymentType(sale.getPaymentType());
        salePayment.setPaymentDescription(sale.getPaymentDescription());
        salePayment.setSale(sale);

        salePayment.setCreatedAt(LocalDateTime.now());
        salePayment.setUpdateAt(LocalDateTime.now());

        return salePayment;
    }
}
