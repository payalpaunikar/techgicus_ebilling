package com.example.techgicus_ebilling.techgicus_ebilling.mapper;

import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.StockTransaction;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.itemDto.StockTransactionDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StockTransactionMapper {

    StockTransactionDto toDto(StockTransaction stockTransaction);
}
