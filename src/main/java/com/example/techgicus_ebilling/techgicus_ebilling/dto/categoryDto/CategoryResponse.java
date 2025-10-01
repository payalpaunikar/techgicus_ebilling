package com.example.techgicus_ebilling.techgicus_ebilling.dto.categoryDto;



public class CategoryResponse {
    private Long categoryId;
    private String categoryName;

    public CategoryResponse() {
    }

    public CategoryResponse(Long categoryId, String categoryName) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
