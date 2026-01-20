package com.example.techgicus_ebilling.techgicus_ebilling.imports.mapper;

import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Item;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.SaleReturn;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.SaleReturnItem;
import com.example.techgicus_ebilling.techgicus_ebilling.imports.dto.SaleItemRow;
import org.springframework.stereotype.Component;

@Component
public class SaleReturnItemEntityMapper {


    public SaleReturnItem toEntity(SaleReturn saleReturn,
                                   SaleItemRow data,
                                   Item item,
                                   SaleReturnItem existingSaleReturnItem){

        SaleReturnItem saleReturnItem = existingSaleReturnItem !=null?existingSaleReturnItem:new SaleReturnItem();

        saleReturnItem.setItem(item);
        saleReturnItem.setQuantity(data.getQuantity());
        saleReturnItem.setRatePerUnit(data.getUnitPrice());
        saleReturnItem.setTaxRate(data.getTaxPercentage());
        saleReturnItem.setTotalTaxAmount(data.getTaxPrice());
        saleReturnItem.setTotalAmount(data.getAmount());
        saleReturnItem.setSaleReturn(saleReturn);

        return saleReturnItem;
    }

}
