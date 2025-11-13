package com.example.techgicus_ebilling.techgicus_ebilling.controller;

import com.example.techgicus_ebilling.techgicus_ebilling.dto.expenseItemDto.ExpenseItemRequest;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.expenseItemDto.ExpenseItemWithExpenseAmountResponse;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.expenseItemDto.ExpensesItemResponse;
import com.example.techgicus_ebilling.techgicus_ebilling.service.ExpenseItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
public class ExpenseItemController {

       private ExpenseItemService expenseItemService;

    @Autowired
    public ExpenseItemController(ExpenseItemService expenseItemService) {
        this.expenseItemService = expenseItemService;
    }


    @PostMapping("/company/{companyId}/create/expense-item")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ExpensesItemResponse> createExpenseItem(@PathVariable Long companyId, @RequestBody ExpenseItemRequest expenseItemRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(expenseItemService.createExpenseItem(companyId,expenseItemRequest));
    }


    @GetMapping("/company/{companyId}/expense-item/list")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<ExpensesItemResponse>> getExpenseItemListByCompanyId(@PathVariable Long companyId){
        return ResponseEntity.ok(expenseItemService.getExpenseItemListByCompanyId(companyId));
    }


    @GetMapping("/expense-item/{expenseItemId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ExpensesItemResponse> getExpenseItemById(@PathVariable Long expenseItemId){
        return ResponseEntity.ok(expenseItemService.getExpenseItemById(expenseItemId));
    }

    @PutMapping("/expense-item/{expenseItemId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ExpensesItemResponse> updateExpenseItemById(@PathVariable Long expenseItemId,@RequestBody ExpenseItemRequest expenseItemRequest){
        return ResponseEntity.ok(expenseItemService.updateExpenseItemById(expenseItemId,expenseItemRequest));
    }

    @DeleteMapping("/expense-item/{expenseItemId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> deleteExpenseItemById(@PathVariable Long expenseItemId){
        return ResponseEntity.ok(expenseItemService.deleteExpenseItemById(expenseItemId));
    }


    @GetMapping("/company/{companyId}/expenses-items/with/expense-amount/by/filter")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<ExpenseItemWithExpenseAmountResponse> getExpensesItemListWithExpenseAmount(@PathVariable Long companyId,
                                                                                           @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)LocalDate startDate,
                                                                                           @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate){
       return expenseItemService.getExpensesItemListWithExpenseAmount(companyId,startDate,endDate);
    }
}
