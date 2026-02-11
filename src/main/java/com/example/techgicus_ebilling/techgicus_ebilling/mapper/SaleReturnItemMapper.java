package com.example.techgicus_ebilling.techgicus_ebilling.mapper;


import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.SaleReturnItem;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.saleReturnDto.SaleReturnItemRequest;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.saleReturnDto.SaleReturnItemResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SaleReturnItemMapper {

    @Mapping(source = "itemDescription", target = "itemDescription")
    SaleReturnItem convertRequestToEntity(SaleReturnItemRequest saleReturnItemRequest);

    @Mapping(source = "itemDescription", target = "itemDescription")
    @Mapping(
            target = "itemHsnCode",
            source = "item.itemHsn"   // 🔥 THIS LINE
    )
    SaleReturnItemResponse convertEntityToResponse(SaleReturnItem saleReturnItem);

    List<SaleReturnItemResponse> convertEntityListToResponseList(List<SaleReturnItem> saleReturnItems);
}
