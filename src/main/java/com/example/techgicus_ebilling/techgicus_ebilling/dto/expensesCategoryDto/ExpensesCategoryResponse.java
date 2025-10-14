package com.example.techgicus_ebilling.techgicus_ebilling.dto.expensesCategoryDto;

public class ExpensesCategoryResponse {
    private Long expensesCategoryId;

    private String categoryName;

    public ExpensesCategoryResponse() {
    }

    public ExpensesCategoryResponse(Long expensesCategoryId, String categoryName) {
        this.expensesCategoryId = expensesCategoryId;
        this.categoryName = categoryName;
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
}
