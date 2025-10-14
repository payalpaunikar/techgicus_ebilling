package com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity;

import jakarta.persistence.*;


@Entity
public class ExpensesCategory {

      @Id
      @GeneratedValue(strategy = GenerationType.IDENTITY)
      private Long expensesCategoryId;

      private String categoryName;

      @ManyToOne
      @JoinColumn(name = "company_id")
      private Company company;

    public ExpensesCategory() {
    }

    public ExpensesCategory(Long expensesCategoryId, String categoryName, Company company) {
        this.expensesCategoryId = expensesCategoryId;
        this.categoryName = categoryName;
        this.company = company;
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

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }
}
