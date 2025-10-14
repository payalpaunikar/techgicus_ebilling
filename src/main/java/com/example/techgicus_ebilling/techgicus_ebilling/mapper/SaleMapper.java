package com.example.techgicus_ebilling.techgicus_ebilling.mapper;

import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Sale;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.saleDto.SaleRequest;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.saleDto.SaleResponse;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SaleMapper {



    Sale convertSaleRequestIntoSale(SaleRequest saleRequest);

    SaleResponse convertSaleIntoSaleResponse(Sale sale);

    List<SaleResponse> convertSaleListIntoSaleResponseList(List<Sale> sales);


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateSaleFromDto(SaleRequest saleRequest, @MappingTarget Sale sale);

}
