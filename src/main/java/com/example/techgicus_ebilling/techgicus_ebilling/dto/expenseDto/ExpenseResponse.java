package com.example.techgicus_ebilling.techgicus_ebilling.dto.expenseDto;

import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.PaymentType;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.addExpenseItem.AddExpenseItemResponse;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.expensesCategoryDto.ExpensesCategoryResponse;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ExpenseResponse {
    private Long expenseNo;
    private ExpensesCategoryResponse expensesCategoryResponse;
    private LocalDate expenseDate;
    private Double totalAmount;
    private PaymentType paymentType;
    private String description;
    List<AddExpenseItemResponse> addExpenseItemResponses = new ArrayList<>();

    public Long getExpenseNo() {
        return expenseNo;
    }

    public void setExpenseNo(Long expenseNo) {
        this.expenseNo = expenseNo;
    }

    public ExpensesCategoryResponse getExpensesCategoryResponse() {
        return expensesCategoryResponse;
    }

    public void setExpensesCategoryResponse(ExpensesCategoryResponse expensesCategoryResponse) {
        this.expensesCategoryResponse = expensesCategoryResponse;
    }

    public LocalDate getExpenseDate() {
        return expenseDate;
    }

    public void setExpenseDate(LocalDate expenseDate) {
        this.expenseDate = expenseDate;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<AddExpenseItemResponse> getAddExpenseItemResponses() {
        return addExpenseItemResponses;
    }

    public void setAddExpenseItemResponses(List<AddExpenseItemResponse> addExpenseItemResponses) {
        this.addExpenseItemResponses = addExpenseItemResponses;
    }
}
