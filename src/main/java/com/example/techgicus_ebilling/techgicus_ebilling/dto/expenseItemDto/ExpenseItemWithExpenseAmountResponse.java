package com.example.techgicus_ebilling.techgicus_ebilling.dto.expenseItemDto;

public class ExpenseItemWithExpenseAmountResponse {
    private Long expenseItemId;

    private String name;

    private Double totalExpenseAmount;

    public ExpenseItemWithExpenseAmountResponse(Long expenseItemId, String name, Double totalExpenseAmount) {
        this.expenseItemId = expenseItemId;
        this.name = name;
        this.totalExpenseAmount = totalExpenseAmount;
    }

    public Long getExpenseItemId() {
        return expenseItemId;
    }

    public void setExpenseItemId(Long expenseItemId) {
        this.expenseItemId = expenseItemId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getTotalExpenseAmount() {
        return totalExpenseAmount;
    }

    public void setTotalExpenseAmount(Double totalExpenseAmount) {
        this.totalExpenseAmount = totalExpenseAmount;
    }
}
