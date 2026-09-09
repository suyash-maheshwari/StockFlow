package com.Suyash.StockFlow.service;

import com.Suyash.StockFlow.payload.request.CategoryDto;
import com.Suyash.StockFlow.payload.response.pageResponse.BulkCategoryResult;
import com.Suyash.StockFlow.payload.response.pageResponse.CategoryPageResponse;
import com.Suyash.StockFlow.payload.response.CategoryResponse;

import java.util.List;


public interface CategoryService {

    CategoryPageResponse getAllCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    CategoryResponse createCategory(CategoryDto categoryResponseDto);

    String deleteCategory(Long categoryId);

    CategoryResponse updateCategory(CategoryDto categoryDto, Long categoryId);

    CategoryResponse getCategoryById(Long categoryId);

    BulkCategoryResult createCategories(List<CategoryDto> categoryDtos);
}