package com.example.techgicus_ebilling.techgicus_ebilling.mapper;

import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.DeliveryChallanItem;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.deliveryChallanDto.DeliveryChallanItemRequest;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.deliveryChallanDto.DeliveryChallanItemResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DeliveryChallanItemMapper {

    @Mapping(source = "itemDescription", target = "itemDescription")
    DeliveryChallanItem convertRequestToEntity(DeliveryChallanItemRequest deliveryChallanItemRequest);

    @Mapping(source = "itemDescription", target = "itemDescription")
    @Mapping(
            target = "itemHsnCode",
            source = "item.itemHsn"   // 🔥 THIS LINE
    )
    DeliveryChallanItemResponse convertEntityToResponse(DeliveryChallanItem deliveryChallanItem);

    List<DeliveryChallanItemResponse> convertEntityListToResponseList(List<DeliveryChallanItem> deliveryChallanItems);

}
