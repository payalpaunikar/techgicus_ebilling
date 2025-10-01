package com.example.techgicus_ebilling.techgicus_ebilling.mapper;

import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Sale;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.saleDto.SaleRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SaleMapper {

    Sale convertSaleRequestIntoSale(SaleRequest saleRequest);
}
