package com.example.techgicus_ebilling.techgicus_ebilling.service;


import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Company;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.ExpensesCategory;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.expensesCategoryDto.ExpensesCategoryResponse;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.expensesCategoryDto.ExpensesCategoryWithExpenseAmountResponse;
import com.example.techgicus_ebilling.techgicus_ebilling.exception.ResourceNotFoundException;
import com.example.techgicus_ebilling.techgicus_ebilling.mapper.ExpensesCategoryMapper;
import com.example.techgicus_ebilling.techgicus_ebilling.repository.CompanyRepository;
import com.example.techgicus_ebilling.techgicus_ebilling.repository.ExpenseRepository;
import com.example.techgicus_ebilling.techgicus_ebilling.repository.ExpensesCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExpensesCategoryService {

       private ExpensesCategoryRepository expensesCategoryRepository;
       private ExpensesCategoryMapper expensesCategoryMapper;
       private CompanyRepository companyRepository;
       private ExpenseRepository expenseRepository;

    @Autowired
    public ExpensesCategoryService(ExpensesCategoryRepository expensesCategoryRepository, ExpensesCategoryMapper expensesCategoryMapper, CompanyRepository companyRepository, ExpenseRepository expenseRepository) {
        this.expensesCategoryRepository = expensesCategoryRepository;
        this.expensesCategoryMapper = expensesCategoryMapper;
        this.companyRepository = companyRepository;
        this.expenseRepository = expenseRepository;
    }

    public ExpensesCategoryResponse createExpensesCategory(Long companyId, String categoryName){
        Company company = companyRepository.findById(companyId)
                .orElseThrow(()-> new ResourceNotFoundException("Company not found with id : "+companyId));

        ExpensesCategory expensesCategory = new ExpensesCategory();
        expensesCategory.setCompany(company);
        expensesCategory.setCategoryName(categoryName);

        ExpensesCategory saveExpensesCategory = expensesCategoryRepository.save(expensesCategory);

        ExpensesCategoryResponse expensesCategoryResponse = expensesCategoryMapper.convertExpensesCategoryIntoExpensesCategoryResponse(saveExpensesCategory);

        return expensesCategoryResponse;
    }

    public List<ExpensesCategoryResponse> getAllExpensesCategoryByCompanyId(Long companyId){
        Company company = companyRepository.findById(companyId)
                .orElseThrow(()-> new ResourceNotFoundException("Company not found with id : "+companyId));

        List<ExpensesCategory> categories = expensesCategoryRepository.findAllByCompany(company);

        List<ExpensesCategoryResponse> categoryResponses = expensesCategoryMapper.convertExpensesCategoryListIntoExpensesCategoryResponseList(categories);

        return categoryResponses;
    }


    public ExpensesCategoryResponse getExpensesCategoryById(Long expensesCategoryId){
        ExpensesCategory expensesCategory = expensesCategoryRepository.findById(expensesCategoryId)
                .orElseThrow(()-> new ResourceNotFoundException("Expenses Category not found by id : "+expensesCategoryId));

        ExpensesCategoryResponse expensesCategoryResponse = expensesCategoryMapper.convertExpensesCategoryIntoExpensesCategoryResponse(expensesCategory);

        return expensesCategoryResponse;
    }


    public ExpensesCategoryResponse updateExpensesCategoryById(Long expensesCategoryId, String categoryName){
        ExpensesCategory expensesCategory = expensesCategoryRepository.findById(expensesCategoryId)
                .orElseThrow(()-> new ResourceNotFoundException("Expenses Category not found by id : "+expensesCategoryId));

        expensesCategory.setCategoryName(categoryName);

        expensesCategoryRepository.save(expensesCategory);

        ExpensesCategoryResponse expensesCategoryResponse = expensesCategoryMapper.convertExpensesCategoryIntoExpensesCategoryResponse(expensesCategory);

        return expensesCategoryResponse;
    }

    public String deleteExpensesCategoryById(Long expensesCategoryId){
        ExpensesCategory expensesCategory = expensesCategoryRepository.findById(expensesCategoryId)
                .orElseThrow(()-> new ResourceNotFoundException("Expenses Category not found by id : "+expensesCategoryId));

       expensesCategoryRepository.delete(expensesCategory);

       return "Expenses category delete successfully.";
    }


    public List<ExpensesCategoryWithExpenseAmountResponse> getExpensesCategoriesWithExpenseAmount(Long companyId, LocalDate startDate, LocalDate endDate){
        Company company = companyRepository.findById(companyId)
                .orElseThrow(()-> new ResourceNotFoundException("Company not found with id : "+companyId));

        List<ExpensesCategoryWithExpenseAmountResponse> expensesCategoryWithExpenseAmountResponse = new ArrayList<>();

        if (startDate == null && endDate == null) {
             expensesCategoryWithExpenseAmountResponse = expenseRepository.findAllExpensesCategoryWithExpenseAmount(company.getCompanyId());
        }
        else{
           expensesCategoryWithExpenseAmountResponse = expenseRepository.findAllExpensesCategoryWithExpenseAmountByDate(company.getCompanyId(),startDate,endDate);
        }

        return expensesCategoryWithExpenseAmountResponse;
    }
}
