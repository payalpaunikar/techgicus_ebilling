package com.example.techgicus_ebilling.techgicus_ebilling.imports.mapper;

import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.*;
import com.example.techgicus_ebilling.techgicus_ebilling.imports.dto.SaleItemRow;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class SaleItemEntityMapper {

    public SaleItem toEntity(Sale sale, SaleItemRow data,
                             Item item,SaleItem existingSaleItem){

        SaleItem saleItem = existingSaleItem !=null ? existingSaleItem:new SaleItem();

        saleItem.setItem(item);
        saleItem.setQuantity(data.getQuantity());
        saleItem.setPricePerUnit(data.getUnitPrice());
        saleItem.setTaxRate(data.getTaxPercentage());
        saleItem.setTaxAmount(data.getTaxPrice());
        saleItem.setTotalAmount(data.getAmount());
        saleItem.setSale(sale);

        if (existingSaleItem == null){
            saleItem.setCreatedAt(LocalDateTime.now());
        }

        saleItem.setUpdateAt(LocalDateTime.now());

        return saleItem;

    }
}
