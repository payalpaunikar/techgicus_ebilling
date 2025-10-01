package com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


@Entity
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;

    @Column(nullable = false)
    private String categoryName;

    @ManyToMany(mappedBy = "categories")
    private Set<Item> items = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    private LocalDateTime craetedAt;

    private LocalDateTime updatedAt;

    public Category() {
    }

    public Category(Long categoryId, String categoryName, Set<Item> items, Company company, LocalDateTime craetedAt, LocalDateTime updatedAt) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.items = items;
        this.company = company;
        this.craetedAt = craetedAt;
        this.updatedAt = updatedAt;
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

    public LocalDateTime getCraetedAt() {
        return craetedAt;
    }

    public void setCraetedAt(LocalDateTime craetedAt) {
        this.craetedAt = craetedAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Set<Item> getItems() {
        return items;
    }

    public void setItems(Set<Item> items) {
        this.items = items;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }
}
