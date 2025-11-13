package com.example.techgicus_ebilling.techgicus_ebilling.controller;


import com.example.techgicus_ebilling.techgicus_ebilling.dto.expenseDto.ExpenseRequest;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.expenseDto.ExpenseResponse;
import com.example.techgicus_ebilling.techgicus_ebilling.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ExpenseController {

      private ExpenseService expenseService;

    @Autowired
    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }


    @PostMapping("/company/{companyId}/create-expense")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ExpenseResponse> createExpense(@PathVariable Long companyId, @RequestBody ExpenseRequest expenseRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(expenseService.createExpense(companyId,expenseRequest));
    }


    @GetMapping("/expense/{expenseId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ExpenseResponse> getExpenseById(@PathVariable Long expenseId){
        return ResponseEntity.ok(expenseService.getExpenseById(expenseId));
    }


    @GetMapping("/company/{companyId}/get/all/expense")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<ExpenseResponse>> getAllExpenses(@PathVariable Long companyId){
        return ResponseEntity.ok(expenseService.getAllExpense(companyId));
    }


    @DeleteMapping("/expense/{expenseId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> deleteExpenseById(@PathVariable Long expenseId){
        return ResponseEntity.ok(expenseService.deleteExpenseById(expenseId));
    }

    @PutMapping("/expense/{expenseId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ExpenseResponse> updateExpenseById(@PathVariable Long expenseId,@RequestBody ExpenseRequest expenseRequest){
       return ResponseEntity.ok(expenseService.updateExpenseById(expenseId,expenseRequest));
    }

}
