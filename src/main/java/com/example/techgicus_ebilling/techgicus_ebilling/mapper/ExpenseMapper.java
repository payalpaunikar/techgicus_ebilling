package com.example.techgicus_ebilling.techgicus_ebilling.mapper;


import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Expense;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.expenseDto.ExpenseRequest;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.expenseDto.ExpenseResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ExpenseMapper {

    Expense convertRequestIntoEntity(ExpenseRequest expenseRequest);

    ExpenseResponse convertEntityIntoResponse(Expense expense);
}
