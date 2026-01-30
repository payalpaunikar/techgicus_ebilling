package com.example.techgicus_ebilling.techgicus_ebilling.imports.mapper;


import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Company;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Party;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Purchase;
import com.example.techgicus_ebilling.techgicus_ebilling.imports.dto.PurchaseRowData;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class PurchaseEntityMapper {

       public Purchase toEntity(Purchase existingPurchase, PurchaseRowData data, Company company, Party party){

           Purchase purchase = existingPurchase != null ? existingPurchase : new Purchase();

           purchase.setBillDate(data.getBillDate());
           purchase.setBillNumber(data.getBillNo());
           purchase.setTotalAmount(data.getTotalAmount());
           purchase.setPaymentType(data.getPaymentType());
           purchase.setSendAmount(data.getPaidAmount());
           purchase.setBalance(data.getBalance());
           purchase.setDueDate(data.getDueDate());
           purchase.setIsPaid(data.isPaid());
           purchase.setPaymentDescription(data.getDescription());
           purchase.setCompany(company);
           purchase.setParty(party);

           if (existingPurchase == null){
               purchase.setCreatedAt(LocalDateTime.now());
           }

           purchase.setUpdateAt(LocalDateTime.now());

           return purchase;
       }
}
