package com.example.techgicus_ebilling.techgicus_ebilling.mapper;

import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.PurchaseReturnItem;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.purchaseReturnDto.PurchaseReturnItemRequest;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.purchaseReturnDto.PurchaseReturnItemResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PurchaseReturnItemMapper {

    @Mapping(source = "itemDescription", target = "itemDescription")
    PurchaseReturnItem convertRequestToEntity(PurchaseReturnItemRequest purchaseReturnItemRequest);

    @Mapping(source = "itemDescription", target = "itemDescription")
    @Mapping(
            target = "itemHsnCode",
            source = "item.itemHsn"   // 🔥 THIS LINE
    )
    PurchaseReturnItemResponse convertEntityToResponse(PurchaseReturnItem purchaseReturnItem);

    List<PurchaseReturnItemResponse> convertEntityListToResponseList(List<PurchaseReturnItem> purchaseReturnItems);
}
