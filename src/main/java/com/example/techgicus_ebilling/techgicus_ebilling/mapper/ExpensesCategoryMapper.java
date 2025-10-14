package com.example.techgicus_ebilling.techgicus_ebilling.mapper;


import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.ExpensesCategory;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.expensesCategoryDto.ExpensesCategoryResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ExpensesCategoryMapper {

    ExpensesCategoryResponse convertExpensesCategoryIntoExpensesCategoryResponse(ExpensesCategory expensesCategory);

    List<ExpensesCategoryResponse> convertExpensesCategoryListIntoExpensesCategoryResponseList(List<ExpensesCategory> expensesCategoryList);

}
