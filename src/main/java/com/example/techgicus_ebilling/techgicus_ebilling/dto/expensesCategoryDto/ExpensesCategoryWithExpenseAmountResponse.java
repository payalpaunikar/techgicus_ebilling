package com.example.techgicus_ebilling.techgicus_ebilling.dto.expensesCategoryDto;

public class ExpensesCategoryWithExpenseAmountResponse {
    private Long expensesCategoryId;

    private String categoryName;

    private Double totalExpensesAmount;

    public ExpensesCategoryWithExpenseAmountResponse(Long expensesCategoryId, String categoryName, Double totalExpensesAmount) {
        this.expensesCategoryId = expensesCategoryId;
        this.categoryName = categoryName;
        this.totalExpensesAmount = totalExpensesAmount;
    }

    public Long getExpensesCategoryId() {
        return expensesCategoryId;
    }

    public void setExpensesCategoryId(Long expensesCategoryId) {
        this.expensesCategoryId = expensesCategoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Double getTotalExpensesAmount() {
        return totalExpensesAmount;
    }

    public void setTotalExpensesAmount(Double totalExpensesAmount) {
        this.totalExpensesAmount = totalExpensesAmount;
    }
}
