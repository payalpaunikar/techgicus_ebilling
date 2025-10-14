package com.example.techgicus_ebilling.techgicus_ebilling.mapper;


import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.AddExpenseItem;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.addExpenseItem.AddExpenseItemRequest;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.addExpenseItem.AddExpenseItemResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AddExpenseItemMapper {

    AddExpenseItem convertRequestIntoEntity(AddExpenseItemRequest addExpenseItemRequest);

    AddExpenseItemResponse convertEntityIntoResponse(AddExpenseItem addExpenseItem);


}
