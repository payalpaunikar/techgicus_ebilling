package com.example.techgicus_ebilling.techgicus_ebilling.mapper;

import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.DeliveryChallanItem;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.deliveryChallanDto.DeliveryChallanItemRequest;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.deliveryChallanDto.DeliveryChallanItemResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DeliveryChallanItemMapper {

    DeliveryChallanItem convertRequestToEntity(DeliveryChallanItemRequest deliveryChallanItemRequest);

    DeliveryChallanItemResponse convertEntityToResponse(DeliveryChallanItem deliveryChallanItem);

    List<DeliveryChallanItemResponse> convertEntityListToResponseList(List<DeliveryChallanItem> deliveryChallanItems);

}
