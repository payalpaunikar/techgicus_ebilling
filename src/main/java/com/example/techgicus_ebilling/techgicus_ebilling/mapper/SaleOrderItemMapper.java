package com.example.techgicus_ebilling.techgicus_ebilling.mapper;


import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.SaleOrderItem;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.saleOrderDto.SaleOrderItemRequest;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.saleOrderDto.SaleOrderItemResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SaleOrderItemMapper {

    @Mapping(source = "itemDescription", target = "itemDescription")
    SaleOrderItem convertRequestToEntity(SaleOrderItemRequest saleOrderItemRequest);

    @Mapping(source = "itemDescription", target = "itemDescription")
    @Mapping(
            target = "itemHsnCode",
            source = "item.itemHsn"   // 🔥 THIS LINE
    )
    SaleOrderItemResponse convertEntityToResponse(SaleOrderItem saleOrderItem);

    List<SaleOrderItemResponse> convertEntityListToResponseList(List<SaleOrderItem> saleOrderItems);
}
