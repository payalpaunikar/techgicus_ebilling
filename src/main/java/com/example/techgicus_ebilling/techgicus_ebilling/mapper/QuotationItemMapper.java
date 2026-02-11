package com.example.techgicus_ebilling.techgicus_ebilling.mapper;


import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.QuotationItem;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.quotationDto.QuotationItemRequest;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.quotationDto.QuotationItemResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface QuotationItemMapper {

    QuotationItem convertQuotationItemRequestIntoQuotationItem(QuotationItemRequest quotationItemRequest);

    List<QuotationItem> convertQuotationItemRequestListIntoQuotationItemList(List<QuotationItemRequest> quotationItemRequests);

    @Mapping(
            target = "itemHsnCode",
            source = "item.itemHsn"   // 🔥 THIS LINE
    )
    QuotationItemResponse convertQuotationItemIntoQuotationItemResponse(QuotationItem quotationItem);

    List<QuotationItemResponse> convertQuotationItemListIntoQuotationItemResponseList(List<QuotationItem> quotationItems);

}
