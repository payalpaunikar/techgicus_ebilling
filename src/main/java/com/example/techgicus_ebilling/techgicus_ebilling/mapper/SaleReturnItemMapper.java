package com.example.techgicus_ebilling.techgicus_ebilling.mapper;


import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.SaleReturnItem;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.saleReturnDto.SaleReturnItemRequest;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.saleReturnDto.SaleReturnItemResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SaleReturnItemMapper {

    SaleReturnItem convertRequestToEntity(SaleReturnItemRequest saleReturnItemRequest);

    SaleReturnItemResponse convertEntityToResponse(SaleReturnItem saleReturnItem);

    List<SaleReturnItemResponse> convertEntityListToResponseList(List<SaleReturnItem> saleReturnItems);
}
