package com.example.techgicus_ebilling.techgicus_ebilling.mapper;


import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.SaleOrderItem;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.saleOrderDto.SaleOrderItemRequest;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.saleOrderDto.SaleOrderItemResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SaleOrderItemMapper {

    SaleOrderItem convertRequestToEntity(SaleOrderItemRequest saleOrderItemRequest);

    SaleOrderItemResponse convertEntityToResponse(SaleOrderItem saleOrderItem);

    List<SaleOrderItemResponse> convertEntityListToResponseList(List<SaleOrderItem> saleOrderItems);
}
