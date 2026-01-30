package com.example.techgicus_ebilling.techgicus_ebilling.imports.mapper;

import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Company;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Party;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.SaleReturn;
import com.example.techgicus_ebilling.techgicus_ebilling.imports.dto.SaleRowData;
import org.springframework.stereotype.Component;

@Component
public class SaleReturnEntityMapper {

    public SaleReturn toEntity(SaleReturn existingSaleReturn, SaleRowData data, Company company, Party party){

        SaleReturn saleReturn = (existingSaleReturn !=null) ? existingSaleReturn : new SaleReturn();

        saleReturn.setReturnNo(data.getInvoiceNo());
        saleReturn.setReturnDate(data.getInvoiceDate());
        saleReturn.setTotalAmount(data.getTotalAmount());
        saleReturn.setPaymentType(data.getPaymentType());
        saleReturn.setPaidAmount(data.getReceivedAmount());
        saleReturn.setBalanceAmount(data.getBalance());
        saleReturn.setDescription(data.getDescription());
        saleReturn.setCompany(company);
        saleReturn.setParty(party);
        saleReturn.setPhoneNo(data.getPartyPhone());


       return saleReturn;
    }
}
