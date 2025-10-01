package com.example.techgicus_ebilling.techgicus_ebilling.service;


import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Category;
import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Company;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.categoryDto.CategoryResponse;
import com.example.techgicus_ebilling.techgicus_ebilling.exception.ResourceNotFoundException;
import com.example.techgicus_ebilling.techgicus_ebilling.mapper.CategoryMapper;
import com.example.techgicus_ebilling.techgicus_ebilling.repository.CategoryRepository;
import com.example.techgicus_ebilling.techgicus_ebilling.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CategoryService {

         private CategoryRepository categoryRepository;
         private CategoryMapper categoryMapper;
         private CompanyRepository companyRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository, CategoryMapper categoryMapper, CompanyRepository companyRepository) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
        this.companyRepository = companyRepository;
    }

    public CategoryResponse createCategoryInComapny(Long companyId,String categoryName){

        Company company = companyRepository.findById(companyId)
                .orElseThrow(()-> new ResourceNotFoundException("Company not found with id : "+companyId));

        Category category = new Category();
        category.setCategoryName(categoryName);
        category.setCompany(company);
        category.setCraetedAt(LocalDateTime.now());
        category.setUpdatedAt(LocalDateTime.now());

       Category saveCategory = categoryRepository.save(category);

       CategoryResponse categoryResponse = categoryMapper.convertCategoryIntoCategoryResponse(saveCategory);

       return categoryResponse;
    }


    public List<CategoryResponse> getCategoriesByCompanyId(Long companyId){
        Company company = companyRepository.findById(companyId)
                .orElseThrow(()-> new ResourceNotFoundException("Company not found with id : "+companyId));


        List<Category> categories = categoryRepository.findAllByCompany(company);

        List<CategoryResponse> categoryResponses = categoryMapper.convertCategoryListIntoCategoryResponses(categories);

        return categoryResponses;
    }

    public CategoryResponse getCategoryById(Long categoryId){
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(()-> new ResourceNotFoundException("Category not fount with id : "+categoryId));

        CategoryResponse categoryResponse = categoryMapper.convertCategoryIntoCategoryResponse(category);

        return categoryResponse;
    }


    public CategoryResponse updateCategoryById(Long categoryId,String categoryName){
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(()-> new ResourceNotFoundException("Category not fount with id : "+categoryId));

        category.setCategoryName(categoryName);
        category.setUpdatedAt(LocalDateTime.now());

        Category saveCategory = categoryRepository.save(category);

        CategoryResponse categoryResponse = categoryMapper.convertCategoryIntoCategoryResponse(saveCategory);

        return categoryResponse;
    }


    public String deleteCategoryById(Long categoryId){
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(()-> new ResourceNotFoundException("Category not fount with id : "+categoryId));

        categoryRepository.delete(category);

        return "Category delete succefully.";
    }
}
