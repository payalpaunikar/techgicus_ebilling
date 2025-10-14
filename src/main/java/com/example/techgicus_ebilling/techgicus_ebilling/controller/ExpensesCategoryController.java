package com.example.techgicus_ebilling.techgicus_ebilling.controller;


import com.example.techgicus_ebilling.techgicus_ebilling.dto.expensesCategoryDto.ExpensesCategoryResponse;
import com.example.techgicus_ebilling.techgicus_ebilling.service.ExpensesCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ExpensesCategoryController {

    private ExpensesCategoryService expensesCategoryService;

    @Autowired
    public ExpensesCategoryController(ExpensesCategoryService expensesCategoryService) {
        this.expensesCategoryService = expensesCategoryService;
    }


    @PostMapping("/company/{companyId}/create/expenses-category")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ExpensesCategoryResponse> createExpensesCategory(@PathVariable Long companyId, @RequestParam String categoryName){
        return ResponseEntity.status(HttpStatus.CREATED).body(expensesCategoryService.createExpensesCategory(companyId,categoryName));
    }

    @GetMapping("/company/{companyId}/expenses-categories")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<ExpensesCategoryResponse>> getCompanyExpensesCategories(@PathVariable Long companyId){
        return ResponseEntity.ok(expensesCategoryService.getAllExpensesCategoryByCompanyId(companyId));
    }

    @GetMapping("/expenses-category/{expensesCategoryId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ExpensesCategoryResponse> getExpensesCategoryById(@PathVariable Long expensesCategoryId){
        return ResponseEntity.ok(expensesCategoryService.getExpensesCategoryById(expensesCategoryId));
    }


    @PutMapping("/expenses-category/{expensesCategoryId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ExpensesCategoryResponse> updateExpensesCategoryById(@PathVariable Long expensesCategoryId,@RequestParam String categoryName){
        return ResponseEntity.ok(expensesCategoryService.updateExpensesCategoryById(expensesCategoryId,categoryName));
    }

    @DeleteMapping("/expenses-category/{expensesCategoryId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> deleteExpensesCategoryById(@PathVariable Long expensesCategoryId){
        return ResponseEntity.ok(expensesCategoryService.deleteExpensesCategoryById(expensesCategoryId));
    }

}
