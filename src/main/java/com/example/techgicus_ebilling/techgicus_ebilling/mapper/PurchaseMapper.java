package com.example.techgicus_ebilling.techgicus_ebilling.mapper;


import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Purchase;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.purchaseDto.PurchaseItemRequest;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.purchaseDto.PurchaseRequest;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.purchaseDto.PurchaseResponse;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface PurchaseMapper {

//    Purchase convertPurchaseRequestIntoPurchase(PurchaseRequest purchaseRequest);
//
//    PurchaseResponse convertPurchaseIntoPurchaseResponse(Purchase purchase);
//
//
//    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
//    void updatePurchaseByDto(PurchaseRequest purchaseRequest, @MappingTarget Purchase purchase);

    Purchase convertPurchaseRequestIntoPurchase(PurchaseRequest purchaseRequest);

//    PurchaseResponse convertPurchaseIntoPurchaseResponse(Purchase purchase);

    PurchaseResponse convertPurchaseIntoPurchaseResponse(Purchase purchase);


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updatePurchaseByDto(PurchaseRequest purchaseRequest, @MappingTarget Purchase purchase);
}
