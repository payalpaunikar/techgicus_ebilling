package com.example.techgicus_ebilling.techgicus_ebilling.mapper;


import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.PurchaseItem;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.purchaseDto.PurchaseItemRequest;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.purchaseDto.PurchaseItemResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PurchaseItemMapper {

    PurchaseItem convertPurchaseItemRequestIntoPurchaseItem(PurchaseItemRequest purchaseItemRequest);

    PurchaseItemResponse convertPurchaseItemIntoResponse(PurchaseItem purchaseItem);

    List<PurchaseItem> convertPurchaseItemRequestListIntoPurchaseItemList(List<PurchaseItemRequest> purchaseItemRequests);

    List<PurchaseItemResponse> convertPurchaseItemsIntoResponseList(List<PurchaseItem> purchaseItems);

}
