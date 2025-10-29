package com.example.techgicus_ebilling.techgicus_ebilling.mapper;


import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.SaleReturn;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.saleReturnDto.SaleReturnRequest;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.saleReturnDto.SaleReturnResponse;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface SaleReturnMapper {

    SaleReturn convertRequestToEntity(SaleReturnRequest saleReturnRequest);

    SaleReturnResponse convertEntityToResponse(SaleReturn saleReturn);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateSaleReturn(SaleReturnRequest saleReturnRequest, @MappingTarget SaleReturn saleReturn);
}
