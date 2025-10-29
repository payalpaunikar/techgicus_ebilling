package com.example.techgicus_ebilling.techgicus_ebilling.mapper;

import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.PurchaseReturn;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.purchaseReturnDto.PurchaseReturnRequest;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.purchaseReturnDto.PurchaseReturnResponse;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface PurchaseReturnMapper {

    PurchaseReturn convertRequestToEntity(PurchaseReturnRequest purchaseReturnRequest);

    PurchaseReturnResponse convertEntityToResponse(PurchaseReturn purchaseReturn);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updatePurchaseReturn(PurchaseReturnRequest purchaseReturnRequest, @MappingTarget PurchaseReturn purchaseReturn);
}
