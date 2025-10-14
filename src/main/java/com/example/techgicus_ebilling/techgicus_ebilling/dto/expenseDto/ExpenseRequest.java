package com.example.techgicus_ebilling.techgicus_ebilling.dto.expenseDto;

import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.AddExpenseItem;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.PaymentType;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.addExpenseItem.AddExpenseItemRequest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ExpenseRequest {
    private Long expenseCategoryId;
    private LocalDate expenseDate;
    List<AddExpenseItemRequest> addExpenseItems = new ArrayList<>();
    private Double totalAmount;
    private PaymentType paymentType;
    private String description;

    public Long getExpenseCategoryId() {
        return expenseCategoryId;
    }

    public void setExpenseCategoryId(Long expenseCategoryId) {
        this.expenseCategoryId = expenseCategoryId;
    }

    public LocalDate getExpenseDate() {
        return expenseDate;
    }

    public void setExpenseDate(LocalDate expenseDate) {
        this.expenseDate = expenseDate;
    }

    public List<AddExpenseItemRequest> getAddExpenseItems() {
        return addExpenseItems;
    }

    public void setAddExpenseItems(List<AddExpenseItemRequest> addExpenseItems) {
        this.addExpenseItems = addExpenseItems;
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
}
