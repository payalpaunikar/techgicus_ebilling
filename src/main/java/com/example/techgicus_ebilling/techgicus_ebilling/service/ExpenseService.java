package com.example.techgicus_ebilling.techgicus_ebilling.service;


import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.*;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.addExpenseItem.AddExpenseItemResponse;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.expenseDto.ExpenseRequest;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.expenseDto.ExpenseResponse;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.expenseItemDto.ExpensesItemResponse;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.expensesCategoryDto.ExpensesCategoryResponse;
import com.example.techgicus_ebilling.techgicus_ebilling.exception.ResourceNotFoundException;
import com.example.techgicus_ebilling.techgicus_ebilling.mapper.AddExpenseItemMapper;
import com.example.techgicus_ebilling.techgicus_ebilling.mapper.ExpenseMapper;
import com.example.techgicus_ebilling.techgicus_ebilling.mapper.ExpensesCategoryMapper;
import com.example.techgicus_ebilling.techgicus_ebilling.repository.CompanyRepository;
import com.example.techgicus_ebilling.techgicus_ebilling.repository.ExpenseItemRepository;
import com.example.techgicus_ebilling.techgicus_ebilling.repository.ExpenseRepository;
import com.example.techgicus_ebilling.techgicus_ebilling.repository.ExpensesCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ExpenseService {

      private ExpenseRepository expenseRepository;
      private ExpenseItemRepository expenseItemRepository;
      private ExpensesCategoryRepository expensesCategoryRepository;
      private CompanyRepository companyRepository;
      private ExpenseMapper expenseMapeer;
      private AddExpenseItemMapper addExpenseItemMapper;
      private ExpensesCategoryMapper expensesCategoryMapper;

    @Autowired
    public ExpenseService(ExpenseRepository expenseRepository, ExpenseItemRepository expenseItemRepository, ExpensesCategoryRepository expensesCategoryRepository, CompanyRepository companyRepository, ExpenseMapper expenseMapeer, AddExpenseItemMapper addExpenseItemMapper, ExpensesCategoryMapper expensesCategoryMapper) {
        this.expenseRepository = expenseRepository;
        this.expenseItemRepository = expenseItemRepository;
        this.expensesCategoryRepository = expensesCategoryRepository;
        this.companyRepository = companyRepository;
        this.expenseMapeer = expenseMapeer;
        this.addExpenseItemMapper = addExpenseItemMapper;
        this.expensesCategoryMapper = expensesCategoryMapper;
    }

    @Transactional
    public ExpenseResponse createExpense(Long companyId, ExpenseRequest expenseRequest){
        Company company = companyRepository.findById(companyId)
                .orElseThrow(()-> new ResourceNotFoundException("Company not found with id : "+companyId));

        ExpensesCategory expensesCategory = expensesCategoryRepository.findById(expenseRequest.getExpenseCategoryId())
                .orElseThrow(()-> new ResourceNotFoundException("Expense Category not found with id : "+expenseRequest.getExpenseCategoryId()));

        Expense expense = expenseMapeer.convertRequestIntoEntity(expenseRequest);
        expense.setExpensesCategory(expensesCategory);
        expense.setCompany(company);
        expense.setCreateAt(LocalDateTime.now());
        expense.setUpdateAt(LocalDateTime.now());

        List<AddExpenseItem> addExpenseItems = expenseRequest.getAddExpenseItems()
                .stream()
                .map(addExpenseItemRequest -> {
                    ExpenseItem expenseItem = expenseItemRepository.findById(addExpenseItemRequest.getExpenseItemId())
                            .orElseGet(()-> new ExpenseItem());
                    AddExpenseItem addExpenseItem = addExpenseItemMapper.convertRequestIntoEntity(addExpenseItemRequest);
                    addExpenseItem.setExpense(expense);
                    addExpenseItem.setExpenseItem(expenseItem);
                    return addExpenseItem;
                }).toList();

        expense.setAddExpenseItems(addExpenseItems);

        expenseRepository.save(expense);

        ExpensesCategoryResponse expensesCategoryResponse = expensesCategoryMapper.convertExpensesCategoryIntoExpensesCategoryResponse(expense.getExpensesCategory());
        List<AddExpenseItemResponse> addExpenseItemResponses = expense.getAddExpenseItems()
                .stream()
                .map(addExpenseItem -> {
                    AddExpenseItemResponse addExpenseItemResponse = addExpenseItemMapper.convertEntityIntoResponse(addExpenseItem);
                    addExpenseItemResponse.setExpenseItemId(addExpenseItem.getExpenseItem().getExpenseItemId());
                    return addExpenseItemResponse;
                }).toList();

        ExpenseResponse expenseResponse = expenseMapeer.convertEntityIntoResponse(expense);
        expenseResponse.setExpensesCategoryResponse(expensesCategoryResponse);
        expenseResponse.setAddExpenseItemResponses(addExpenseItemResponses);

        return expenseResponse;
    }


    public ExpenseResponse getExpenseById(Long expenseId){
        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(()-> new ResourceNotFoundException("Expense not found with id : "+expenseId));

        ExpensesCategoryResponse expensesCategoryResponse = expensesCategoryMapper.convertExpensesCategoryIntoExpensesCategoryResponse(expense.getExpensesCategory());
        List<AddExpenseItemResponse> addExpenseItemResponses = expense.getAddExpenseItems()
                .stream()
                .map(addExpenseItem -> {
                    AddExpenseItemResponse addExpenseItemResponse = addExpenseItemMapper.convertEntityIntoResponse(addExpenseItem);
                    addExpenseItemResponse.setExpenseItemId(addExpenseItem.getExpenseItem().getExpenseItemId());
                    return addExpenseItemResponse;
                }).toList();

        ExpenseResponse expenseResponse = expenseMapeer.convertEntityIntoResponse(expense);
        expenseResponse.setAddExpenseItemResponses(addExpenseItemResponses);
        expenseResponse.setExpensesCategoryResponse(expensesCategoryResponse);

        return expenseResponse;
    }


   public List<ExpenseResponse> getAllExpense(Long companyId){
       Company company = companyRepository.findById(companyId)
               .orElseThrow(()-> new ResourceNotFoundException("Company not found with id : "+companyId));

       List<Expense> expenses = expenseRepository.findAll();

       List<ExpenseResponse> expenseResponses = expenses.stream()
               .map(expense -> {
                   ExpensesCategoryResponse expensesCategoryResponse = expensesCategoryMapper.convertExpensesCategoryIntoExpensesCategoryResponse(expense.getExpensesCategory());
                   List<AddExpenseItemResponse> addExpenseItemResponses = expense.getAddExpenseItems()
                           .stream()
                           .map(addExpenseItem -> {
                               AddExpenseItemResponse addExpenseItemResponse = addExpenseItemMapper.convertEntityIntoResponse(addExpenseItem);
                               addExpenseItemResponse.setExpenseItemId(addExpenseItem.getExpenseItem().getExpenseItemId());
                               return addExpenseItemResponse;
                           }).toList();
                   ExpenseResponse expenseResponse = expenseMapeer.convertEntityIntoResponse(expense);
                   expenseResponse.setExpensesCategoryResponse(expensesCategoryResponse);
                   expenseResponse.setAddExpenseItemResponses(addExpenseItemResponses);

                   return expenseResponse;
               }).toList();

       return expenseResponses;

   }


   public String deleteExpenseById(Long expenseId){
        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(()-> new ResourceNotFoundException("Expense not found with id : "+expenseId));

        expenseRepository.delete(expense);

        return "Expense delete successfully.";
   }

   public ExpenseResponse updateExpenseById(Long expenseId,ExpenseRequest expenseRequest){
       Expense expense = expenseRepository.findById(expenseId)
               .orElseThrow(()-> new ResourceNotFoundException("Expense not found with id : "+expenseId));

       ExpensesCategory expensesCategory = expensesCategoryRepository.findById(expenseRequest.getExpenseCategoryId())
               .orElseThrow(()-> new ResourceNotFoundException("Expense category not found with id : "+expenseRequest.getExpenseCategoryId()));


       List<AddExpenseItem> addExpenseItems = expenseRequest.getAddExpenseItems()
                       .stream()
                               .map(addExpenseItemRequest -> {
                                   ExpenseItem expenseItem = expenseItemRepository.findById(addExpenseItemRequest.getExpenseItemId())
                                           .orElseThrow(()-> new ResourceNotFoundException("Expense Item not found with id : "+addExpenseItemRequest.getExpenseItemId()));
                                   AddExpenseItem addExpenseItem = addExpenseItemMapper.convertRequestIntoEntity(addExpenseItemRequest);
                                   addExpenseItem.setExpense(expense);
                                   addExpenseItem.setExpenseItem(expenseItem);
                                   return addExpenseItem;
                               }).toList();

       expenseMapeer.updateExpense(expenseRequest,expense);
       expense.setExpensesCategory(expensesCategory);
       expense.getAddExpenseItems().clear();
       expense.getAddExpenseItems().addAll(addExpenseItems);
       expense.setUpdateAt(LocalDateTime.now());

       expenseRepository.save(expense);


       ExpensesCategoryResponse expensesCategoryResponse = expensesCategoryMapper.convertExpensesCategoryIntoExpensesCategoryResponse(expense.getExpensesCategory());
       List<AddExpenseItemResponse> addExpenseItemResponses = expense.getAddExpenseItems().stream()
               .map(addExpenseItem -> {
                   AddExpenseItemResponse addExpenseItemResponse = addExpenseItemMapper.convertEntityIntoResponse(addExpenseItem);
                   addExpenseItemResponse.setExpenseItemId(addExpenseItem.getExpenseItem().getExpenseItemId());
                   return addExpenseItemResponse;
               }).toList();
       ExpenseResponse expenseResponse = expenseMapeer.convertEntityIntoResponse(expense);
       expenseResponse.setExpensesCategoryResponse(expensesCategoryResponse);
       expenseResponse.setAddExpenseItemResponses(addExpenseItemResponses);

       return expenseResponse;
   }


}
