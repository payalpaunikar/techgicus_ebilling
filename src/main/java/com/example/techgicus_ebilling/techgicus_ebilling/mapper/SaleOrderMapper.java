package com.example.techgicus_ebilling.techgicus_ebilling.mapper;


import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.SaleOrder;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.saleOrderDto.SaleOrderRequest;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.saleOrderDto.SaleOrderResponse;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface SaleOrderMapper {

//      SaleOrder convertRequestToEntity(SaleOrderRequest saleOrderRequest);
//
//
//
//      SaleOrderResponse convertEntityToResponse(SaleOrder saleOrder);
//
//
//      @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
//      void updateSaleOrder(SaleOrderRequest saleOrderRequest, @MappingTarget SaleOrder saleOrder);

      SaleOrder convertRequestToEntity(SaleOrderRequest saleOrderRequest);



      SaleOrderResponse convertEntityToResponse(SaleOrder saleOrder);


      @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
      void updateSaleOrder(SaleOrderRequest saleOrderRequest, @MappingTarget SaleOrder saleOrder);
}
