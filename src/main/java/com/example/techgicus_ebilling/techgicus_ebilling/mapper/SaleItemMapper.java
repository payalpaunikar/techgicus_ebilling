package com.example.techgicus_ebilling.techgicus_ebilling.mapper;


import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.SaleItem;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.saleDto.SaleItemRequest;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.saleDto.SaleItemResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SaleItemMapper {

//
//    List<SaleItem> convertSaleItemListRequestIntoSaleItemList(List<SaleItemRequest> saleItemRequests);
//
//    SaleItem convertSaleItemRequestIntoSaleItem(SaleItemRequest saleItemRequest);
//
//    List<SaleItemResponse> convertSaleItemListIntoSaleItmResponseList(List<SaleItem> saleItems);
//
//    SaleItemResponse covertSaleItemIntoSaleItemResponse(SaleItem saleItem);


    List<SaleItem> convertSaleItemListRequestIntoSaleItemList(List<SaleItemRequest> saleItemRequests);

    SaleItem convertSaleItemRequestIntoSaleItem(SaleItemRequest saleItemRequest);

    List<SaleItemResponse> convertSaleItemListIntoSaleItmResponseList(List<SaleItem> saleItems);

    SaleItemResponse covertSaleItemIntoSaleItemResponse(SaleItem saleItem);

}
