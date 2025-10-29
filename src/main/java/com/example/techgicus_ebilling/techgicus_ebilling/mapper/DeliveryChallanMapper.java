package com.example.techgicus_ebilling.techgicus_ebilling.mapper;

import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.DeliveryChallan;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.deliveryChallanDto.DeliveryChallanRequest;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.deliveryChallanDto.DeliveryChallanResponse;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface DeliveryChallanMapper {

    DeliveryChallan convertRequestToEntity(DeliveryChallanRequest deliveryChallanRequest);

    DeliveryChallanResponse convertEntityToResponse(DeliveryChallan deliveryChallan);


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateDeliveryChallan(DeliveryChallanRequest deliveryChallanRequest, @MappingTarget DeliveryChallan deliveryChallan);
}
