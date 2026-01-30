package com.example.techgicus_ebilling.techgicus_ebilling.imports.mapper;


import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Item;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.PurchaseReturn;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.PurchaseReturnItem;
import com.example.techgicus_ebilling.techgicus_ebilling.imports.dto.PurchaseItemRow;
import org.springframework.stereotype.Component;

@Component
public class PurchaseReturnItemEntityMapper {

    public PurchaseReturnItem toEntity(PurchaseReturn purchaseReturn,
                                       PurchaseItemRow data,
                                       Item item,
                                       PurchaseReturnItem existingPurchaseReturnItem){

        PurchaseReturnItem purchaseReturnItem = existingPurchaseReturnItem !=null?existingPurchaseReturnItem : new PurchaseReturnItem();

        purchaseReturnItem.setItem(item);
        purchaseReturnItem.setQuantity(data.getQuantity());
        purchaseReturnItem.setRatePerUnit(data.getUnitPrice());
        purchaseReturnItem.setTaxRate(data.getTaxPercentage());
        purchaseReturnItem.setTotalTaxAmount(data.getTaxPrice());
        purchaseReturnItem.setTotalAmount(data.getAmount());
        purchaseReturnItem.setPurchaseReturn(purchaseReturn);

        return purchaseReturnItem;

    }
}
