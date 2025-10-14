package com.example.techgicus_ebilling.techgicus_ebilling.service;


import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Company;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.ExpenseItem;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.expenseItemDto.ExpenseItemRequest;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.expenseItemDto.ExpensesItemResponse;
import com.example.techgicus_ebilling.techgicus_ebilling.exception.ResourceNotFoundException;
import com.example.techgicus_ebilling.techgicus_ebilling.mapper.ExpenseItemMapper;
import com.example.techgicus_ebilling.techgicus_ebilling.repository.CompanyRepository;
import com.example.techgicus_ebilling.techgicus_ebilling.repository.ExpenseItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExpenseItemService {

       private CompanyRepository companyRepository;
       private ExpenseItemRepository expenseItemRepository;
       private ExpenseItemMapper expenseItemMapper;

    @Autowired
    public ExpenseItemService(CompanyRepository companyRepository, ExpenseItemRepository expenseItemRepository, ExpenseItemMapper expenseItemMapper) {
        this.companyRepository = companyRepository;
        this.expenseItemRepository = expenseItemRepository;
        this.expenseItemMapper = expenseItemMapper;
    }


    public ExpensesItemResponse createExpenseItem(Long companyId, ExpenseItemRequest expenseItemRequest){
        Company company = companyRepository.findById(companyId)
                .orElseThrow(()-> new ResourceNotFoundException("Company not found with id : "+companyId));

        ExpenseItem expenseItem = expenseItemMapper.convertRequestIntoEntity(expenseItemRequest);
        expenseItem.setCompany(company);

        expenseItemRepository.save(expenseItem);

        ExpensesItemResponse expensesItemResponse = expenseItemMapper.convertEntityIntoResponse(expenseItem);

        return expensesItemResponse;
    }


    public List<ExpensesItemResponse> getExpenseItemListByCompanyId(Long companyId){
        Company company = companyRepository.findById(companyId)
                .orElseThrow(()-> new ResourceNotFoundException("Company not found with id : "+companyId));

        List<ExpenseItem> expenseItems = expenseItemRepository.findAllByCompany(company);

        List<ExpensesItemResponse> expensesItemResponses = expenseItemMapper.convertEntityListIntoResponseList(expenseItems);

        return expensesItemResponses;
    }



    public ExpensesItemResponse getExpenseItemById(Long expenseItemId){
        ExpenseItem expenseItem = expenseItemRepository.findById(expenseItemId)
                .orElseThrow(()-> new ResourceNotFoundException("Expense Item not found with id : "+expenseItemId));

        ExpensesItemResponse expensesItemResponse = expenseItemMapper.convertEntityIntoResponse(expenseItem);

        return expensesItemResponse;
    }

    public ExpensesItemResponse updateExpenseItemById(Long expenseItemId,ExpenseItemRequest expenseItemRequest){
        ExpenseItem expenseItem = expenseItemRepository.findById(expenseItemId)
                .orElseThrow(()-> new ResourceNotFoundException("Expense Item not found with id : "+expenseItemId));

        expenseItemMapper.updateEntityThroughDto(expenseItemRequest,expenseItem);

        expenseItemRepository.save(expenseItem);

        ExpensesItemResponse expensesItemResponse = expenseItemMapper.convertEntityIntoResponse(expenseItem);

        return expensesItemResponse;
    }

    public String deleteExpenseItemById(Long expenseItemId){
        ExpenseItem expenseItem = expenseItemRepository.findById(expenseItemId)
                .orElseThrow(()-> new ResourceNotFoundException("Expense Item not found with id : "+expenseItemId));

        expenseItemRepository.delete(expenseItem);

        return "Expense Item delete successfully.";
    }
}
