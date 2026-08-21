package com.Suyash.StockFlow.service.serviceImpl;

import com.Suyash.StockFlow.exceptions.ResourceNotFoundException;
import com.Suyash.StockFlow.model.Category;
import com.Suyash.StockFlow.repository.CategoryRepository;
import com.Suyash.StockFlow.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    private List<Category> categories = new ArrayList<>();

    @Override
    public List<Category> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories;
    }

    @Override
    public String createCategory(Category category) {
        categoryRepository.save(category);
        return "Category Added Successfully";
    }

    @Override
    public String deleteCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category with categoryId: "+ categoryId + " not found."));
        categoryRepository.deleteById(categoryId);
        return "Category with categoryId: " + categoryId + " Deleted Successfully";
    }

    @Override
    public Category updateCategory(Category category, Long categoryId) {
        Category updatedCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category with categoryId: "+ categoryId + " not found."));
        updatedCategory.setCategoryName(category.getCategoryName());
        updatedCategory.setDescription(category.getDescription());

        categoryRepository.save(updatedCategory);
        return updatedCategory;
    }

    @Override
    public Category getCategoryById(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category with categoryId: "+ categoryId + " not found."));
        return category;
    }
}
