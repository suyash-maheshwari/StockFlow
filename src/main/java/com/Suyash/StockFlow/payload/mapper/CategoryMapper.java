package com.Suyash.StockFlow.payload.mapper;

import com.Suyash.StockFlow.model.Category;
import com.Suyash.StockFlow.payload.request.CategoryDto;
import com.Suyash.StockFlow.payload.response.CategoryResponse;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public CategoryResponse toResponse(Category category){
        return CategoryResponse.builder()
                .categoryId(category.getCategoryId())
                .categoryName(category.getCategoryName())
                .description(category.getDescription())
                .build();
    }

    public Category toEntity(CategoryDto categoryDto){
        Category category = new Category();
        category.setCategoryName(categoryDto.getCategoryName());
        category.setDescription(categoryDto.getDescription());

        return category;
    }
}
