package com.example.techgicus_ebilling.techgicus_ebilling.mapper;


import com.example.techgicus_ebilling.techgicus_ebilling.datamodel.entity.Category;
import com.example.techgicus_ebilling.techgicus_ebilling.dto.categoryDto.CategoryResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    @Mapping(source = "categoryId",target = "categoryId")
    @Mapping(source = "categoryName",target = "categoryName")
    CategoryResponse convertCategoryIntoCategoryResponse(Category category);

    List<CategoryResponse> convertCategoryListIntoCategoryResponses(List<Category> categories);


}
