package com.Suyash.StockFlow.service;

import com.Suyash.StockFlow.model.Category;

import java.util.List;


public interface CategoryService {

    List<Category> getAllCategories();

    String createCategory(Category category);

    String deleteCategory(Long categoryId);

    Category updateCategory(Category category, Long categoryId);

    Category getCategoryById(Long categoryId);
}
