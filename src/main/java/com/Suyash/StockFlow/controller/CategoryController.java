package com.Suyash.StockFlow.controller;

import com.Suyash.StockFlow.config.AppConstants;
import com.Suyash.StockFlow.payload.request.CategoryDto;
import com.Suyash.StockFlow.payload.response.pageResponse.BulkCategoryResult;
import com.Suyash.StockFlow.payload.response.pageResponse.CategoryPageResponse;
import com.Suyash.StockFlow.payload.response.CategoryResponse;
import com.Suyash.StockFlow.service.CategoryService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public ResponseEntity<CategoryPageResponse> getAllCategories(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) @Min(0) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) @Min(1) @Max(1000) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_CATEGORIES_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder){
        CategoryPageResponse categories = categoryService.getAllCategories(pageNumber,pageSize,sortBy,sortOrder);
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryResponse> getCategoryById(@PathVariable @Min(1) Long categoryId){
        CategoryResponse category = categoryService.getCategoryById(categoryId);
        return new ResponseEntity<>(category, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(@Valid @RequestBody CategoryDto categoryDto){
        CategoryResponse savedCategory = categoryService.createCategory(categoryDto);
        return new ResponseEntity<>(savedCategory, HttpStatus.CREATED);
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<String> deleteCategory(@PathVariable @Min(1) Long categoryId){
        String message = categoryService.deleteCategory(categoryId);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryResponse> updateCategory(@Valid @RequestBody CategoryDto categoryDto, @PathVariable @Min(1) Long categoryId){
        CategoryResponse updatedCategory = categoryService.updateCategory(categoryDto, categoryId);
        return new ResponseEntity<>(updatedCategory, HttpStatus.OK);
    }

    @PostMapping("/bulk")
    public ResponseEntity<BulkCategoryResult> createCategories(@Valid @RequestBody List<CategoryDto> categoryDtos){
        BulkCategoryResult categories = categoryService.createCategories(categoryDtos);
        return new ResponseEntity<>(categories, HttpStatus.CREATED);
    }
}
