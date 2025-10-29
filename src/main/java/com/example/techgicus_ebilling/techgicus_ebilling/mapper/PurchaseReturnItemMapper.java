package com.example.techgicus_ebilling.techgicus_ebilling.mapper;

import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.PurchaseReturnItem;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.purchaseReturnDto.PurchaseReturnItemRequest;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.purchaseReturnDto.PurchaseReturnItemResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PurchaseReturnItemMapper {

    PurchaseReturnItem convertRequestToEntity(PurchaseReturnItemRequest purchaseReturnItemRequest);

    PurchaseReturnItemResponse convertEntityToResponse(PurchaseReturnItem purchaseReturnItem);

    List<PurchaseReturnItemResponse> convertEntityListToResponseList(List<PurchaseReturnItem> purchaseReturnItems);
}
