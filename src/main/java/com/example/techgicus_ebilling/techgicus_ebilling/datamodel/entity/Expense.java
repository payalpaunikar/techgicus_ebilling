package com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity;


import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.enumeration.PaymentType;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Expense {

     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     private Long expenseNo;

     @ManyToOne
     @JoinColumn(name = "expense_category_id")
     private ExpensesCategory expensesCategory;

     private Double totalAmount;

     @Enumerated(EnumType.STRING)
     private PaymentType paymentType;

     private String description;

     private LocalDate expenseDate;


     @OneToMany(mappedBy = "expense",cascade = CascadeType.ALL,orphanRemoval = true)
     private List<AddExpenseItem> addExpenseItems = new ArrayList<>();

     @ManyToOne
     @JoinColumn(name = "company_id")
     private Company company;

     private LocalDateTime createAt;
     private LocalDateTime updateAt;

    public Long getExpenseNo() {
        return expenseNo;
    }

    public void setExpenseNo(Long expenseNo) {
        this.expenseNo = expenseNo;
    }

    public ExpensesCategory getExpensesCategory() {
        return expensesCategory;
    }

    public void setExpensesCategory(ExpensesCategory expensesCategory) {
        this.expensesCategory = expensesCategory;
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

    public LocalDate getExpenseDate() {
        return expenseDate;
    }

    public void setExpenseDate(LocalDate expenseDate) {
        this.expenseDate = expenseDate;
    }

    public List<AddExpenseItem> getAddExpenseItems() {
        return addExpenseItems;
    }

    public void setAddExpenseItems(List<AddExpenseItem> addExpenseItems) {
        this.addExpenseItems = addExpenseItems;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }

    public LocalDateTime getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(LocalDateTime updateAt) {
        this.updateAt = updateAt;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }
}
