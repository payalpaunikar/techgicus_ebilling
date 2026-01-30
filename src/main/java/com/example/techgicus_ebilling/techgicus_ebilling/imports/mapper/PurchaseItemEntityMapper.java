package com.example.techgicus_ebilling.techgicus_ebilling.imports.mapper;


import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Item;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Purchase;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.PurchaseItem;
import com.example.techgicus_ebilling.techgicus_ebilling.imports.dto.PurchaseItemRow;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class PurchaseItemEntityMapper {

    public PurchaseItem toEntity(Purchase purchase, PurchaseItemRow data,
                                 Item item,PurchaseItem existingPurchaseItem){

        PurchaseItem purchaseItem = existingPurchaseItem !=null ? existingPurchaseItem: new PurchaseItem();
        purchaseItem.setItem(item);
        purchaseItem.setQuantity(data.getQuantity());
        purchaseItem.setPricePerUnit(data.getUnitPrice());
        purchaseItem.setTaxRate(data.getTaxPercentage());
        purchaseItem.setTotalTaxAmount(data.getTaxPrice());
        purchaseItem.setTotalAmount(data.getAmount());
        purchaseItem.setPurchase(purchase);

        if (existingPurchaseItem == null){
            purchaseItem.setCreatedAt(LocalDateTime.now());
        }

        purchaseItem.setUpdateAt(LocalDateTime.now());

        return purchaseItem;
    }

}
