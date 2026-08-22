package com.Suyash.StockFlow.service.serviceImpl;

import com.Suyash.StockFlow.exceptions.DuplicateResourceFoundException;
import com.Suyash.StockFlow.exceptions.ResourceNotFoundException;
import com.Suyash.StockFlow.model.Category;
import com.Suyash.StockFlow.payload.CategoryMapper;
import com.Suyash.StockFlow.payload.request.CategoryDto;
import com.Suyash.StockFlow.payload.response.BulkCategoryResult;
import com.Suyash.StockFlow.payload.response.CategoryPageResponse;
import com.Suyash.StockFlow.payload.response.CategoryResponse;
import com.Suyash.StockFlow.repository.CategoryRepository;
import com.Suyash.StockFlow.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public CategoryPageResponse getAllCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {

        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber,pageSize,sortByAndOrder);
        Page<Category> categoryPage = categoryRepository.findAll(pageDetails);

        List<CategoryResponse> categoryResponses = categoryPage.getContent()
                .stream()
                .map(mapper::toResponse)
                .toList();

        return CategoryPageResponse.builder()
                .content(categoryResponses)
                .pageNumber(categoryPage.getNumber())
                .pageSize(categoryPage.getSize())
                .totalElements(categoryPage.getTotalElements())
                .totalPages(categoryPage.getTotalPages())
                .lastPage(categoryPage.isLast())
                .build();

    }

    @Override
    public CategoryResponse createCategory(CategoryDto categoryDto) {
        categoryRepository.findByCategoryNameIgnoreCase(categoryDto.getCategoryName())
                .ifPresent(existing -> {
                    throw new DuplicateResourceFoundException(
                            "Category with name: " + categoryDto.getCategoryName()+ " already exist."
                    );
                });

        Category category = mapper.toEntity(categoryDto);
        Category savedCategory = categoryRepository.save(category);
        return mapper.toResponse(savedCategory);
    }

    @Override
    public String deleteCategory(Long categoryId) {
        categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category with categoryId: "+ categoryId + " not found."));
        categoryRepository.deleteById(categoryId);
        return "Category with categoryId: " + categoryId + " Deleted Successfully";
    }

    @Override
    public CategoryResponse updateCategory(CategoryDto categoryDto, Long categoryId) {
        Category existingCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category with categoryId: "+ categoryId + " not found."));

        categoryRepository.findByCategoryNameIgnoreCase(categoryDto.getCategoryName())
                        .ifPresent(matchedCategory -> {
                            if(!matchedCategory.getCategoryId().equals(categoryId)){
                                throw new DuplicateResourceFoundException("Category with name '" + categoryDto.getCategoryName() + "' already exists."
                                );
                            }
                        });
        existingCategory.setCategoryName(categoryDto.getCategoryName());
        existingCategory.setDescription(categoryDto.getDescription());

        Category category = categoryRepository.save(existingCategory);
        return mapper.toResponse(category);
    }

    @Transactional(readOnly = true)
    @Override
    public CategoryResponse getCategoryById(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category with categoryId: "+ categoryId + " not found."));
        return mapper.toResponse(category);
    }

    @Transactional
    @Override
    public BulkCategoryResult createCategories(List<CategoryDto> categoryDtos) {
        List<CategoryResponse> createdCategories = new ArrayList<>();
        List<String> skipped = new ArrayList<>();

        for(CategoryDto dto: categoryDtos){
            boolean alreadyExists = categoryRepository.findByCategoryNameIgnoreCase(dto.getCategoryName())
                    .isPresent();

            if(alreadyExists) {
                skipped.add(dto.getCategoryName() + " - already exists, skipped.");
                continue;
            }

            try {
                Category savedCategory = categoryRepository.save(mapper.toEntity(dto));
                createdCategories.add(mapper.toResponse(savedCategory));
            } catch (DataIntegrityViolationException e) {
                // Rare race-condition case: another request saved the same name between our check and our save
                skipped.add(dto.getCategoryName() + " — already exists, skipped");
            }
        }
        return BulkCategoryResult.builder()
                .created(createdCategories)
                .skipped(skipped)
                .build();
    }
}
