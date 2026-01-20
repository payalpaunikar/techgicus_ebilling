package com.example.techgicus_ebilling.techgicus_ebilling.imports.mapper;

import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Company;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Party;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Sale;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.SaleType;
import com.example.techgicus_ebilling.techgicus_ebilling.imports.dto.SaleRowData;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class SaleEntityMapper {

    public Sale toEntity(Sale existingSale,SaleRowData data, Company company, Party party) {

        Sale sale = (existingSale != null) ? existingSale : new Sale();


        sale.setInvoceDate(data.getInvoiceDate());
        sale.setInvoiceNumber(data.getInvoiceNo());
        sale.setTotalAmount(data.getTotalAmount());
        sale.setPaymentType(data.getPaymentType());
        sale.setSaleType(SaleType.CASH);
        sale.setReceivedAmount(data.getReceivedAmount());
        sale.setBalance(data.getBalance());
        sale.setDueDate(data.getDueDate());
        sale.setPaid(data.isPaid());
        sale.setPaymentDescription(data.getDescription());
        sale.setCompany(company);
        sale.setParty(party);
        if (existingSale == null) {
            sale.setCreatedAt(LocalDateTime.now());
        }
        sale.setUpdatedAt(LocalDateTime.now());

        return sale;
    }
}

