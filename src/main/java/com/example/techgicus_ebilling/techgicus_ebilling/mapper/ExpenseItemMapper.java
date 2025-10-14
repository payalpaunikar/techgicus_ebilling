package com.example.techgicus_ebilling.techgicus_ebilling.mapper;


import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.ExpenseItem;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.expenseItemDto.ExpenseItemRequest;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.expenseItemDto.ExpensesItemResponse;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ExpenseItemMapper {

    ExpenseItem convertRequestIntoEntity(ExpenseItemRequest expenseItemRequest);

    ExpensesItemResponse convertEntityIntoResponse(ExpenseItem expenseItem);

    List<ExpensesItemResponse> convertEntityListIntoResponseList(List<ExpenseItem> expenseItems);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityThroughDto(ExpenseItemRequest expenseItemRequest,@MappingTarget ExpenseItem expenseItem);

}
