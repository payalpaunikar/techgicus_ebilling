package com.example.techgicus_ebilling.techgicus_ebilling.imports.mapper;


import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Company;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Party;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.PurchaseReturn;
import com.example.techgicus_ebilling.techgicus_ebilling.imports.dto.PurchaseRowData;
import org.springframework.stereotype.Component;

@Component
public class PurchaseReturnEntityMapper {

    public PurchaseReturn toEntity(PurchaseReturn existingPurchaseReturn, PurchaseRowData data, Company company, Party party){

        PurchaseReturn purchaseReturn = existingPurchaseReturn != null ? existingPurchaseReturn : new PurchaseReturn();

        purchaseReturn.setReturnDate(data.getBillDate());
        purchaseReturn.setReturnNo(data.getBillNo());
        purchaseReturn.setTotalAmount(data.getTotalAmount());
        purchaseReturn.setPaymentType(data.getPaymentType());
        purchaseReturn.setReceivedAmount(data.getPaidAmount());
        purchaseReturn.setBalanceAmount(data.getBalance());
        purchaseReturn.setDescription(data.getDescription());
        purchaseReturn.setCompany(company);
        purchaseReturn.setParty(party);
        purchaseReturn.setPhoneNo(data.getPartyPhone());

        return purchaseReturn;
    }
}
