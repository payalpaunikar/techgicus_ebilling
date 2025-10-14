package com.example.techgicus_ebilling.techgicus_ebilling.mapper;

import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.PurchaseOrderItem;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.purchaseOrderDto.PurchaseOrderItemRequest;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.purchaseOrderDto.PurchaseOrderItemResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PurchaseOrderItemMapper {

    PurchaseOrderItem convertRequestToEntity(PurchaseOrderItemRequest purchaseOrderItemRequest);

    PurchaseOrderItemResponse convertEntityToResponse(PurchaseOrderItem purchaseOrderItem);

    List<PurchaseOrderItemResponse> convertEntityListToResponseList(List<PurchaseOrderItem> purchaseOrderItemList);
}
