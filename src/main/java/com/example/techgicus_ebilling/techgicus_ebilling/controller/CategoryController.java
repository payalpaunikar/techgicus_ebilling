package com.example.techgicus_ebilling.techgicus_ebilling.controller;


import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Category;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.categoryDto.CategoryResponse;
import com.example.techgicus_ebilling.techgicus_ebilling.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CategoryController {

      private CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/company/{companyId}/create/category")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<CategoryResponse> createdCategoryInCompany(@PathVariable Long companyId ,@RequestParam String categoryName){
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.createCategoryInComapny(companyId,categoryName));
    }


    @GetMapping("/company/{companyId}/categories")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<CategoryResponse>> getCategoriesByComapnyId(@PathVariable Long companyId){
        return ResponseEntity.ok(categoryService.getCategoriesByCompanyId(companyId));
    }


    @GetMapping("/category/{categoryId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<CategoryResponse> getCategoryById(@PathVariable Long categoryId){
        return ResponseEntity.ok(categoryService.getCategoryById(categoryId));
    }


    @PutMapping("/category/{categoryId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<CategoryResponse> updateCategoryById(@PathVariable Long categoryId,@RequestParam String categoryName){
        return ResponseEntity.ok(categoryService.updateCategoryById(categoryId,categoryName));
    }


    @DeleteMapping("/category/{categoryId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> deleteCategoryById(@PathVariable Long categoryId){
        return ResponseEntity.ok(categoryService.deleteCategoryById(categoryId));
    }
}
