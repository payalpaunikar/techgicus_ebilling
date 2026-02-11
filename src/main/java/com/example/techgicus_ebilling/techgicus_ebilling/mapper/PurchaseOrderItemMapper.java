package com.example.techgicus_ebilling.techgicus_ebilling.mapper;

import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.PurchaseOrderItem;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.purchaseOrderDto.PurchaseOrderItemRequest;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.purchaseOrderDto.PurchaseOrderItemResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PurchaseOrderItemMapper {

    @Mapping(source = "itemDescription", target = "itemDescription")
    PurchaseOrderItem convertRequestToEntity(PurchaseOrderItemRequest purchaseOrderItemRequest);

    @Mapping(source = "itemDescription", target = "itemDescription")
    @Mapping(
            target = "itemHsnCode",
            source = "item.itemHsn"   // 🔥 THIS LINE
    )
    PurchaseOrderItemResponse convertEntityToResponse(PurchaseOrderItem purchaseOrderItem);

    List<PurchaseOrderItemResponse> convertEntityListToResponseList(List<PurchaseOrderItem> purchaseOrderItemList);
}
