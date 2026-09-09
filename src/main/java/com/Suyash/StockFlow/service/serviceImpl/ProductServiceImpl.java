package com.Suyash.StockFlow.service.serviceImpl;

import com.Suyash.StockFlow.exceptions.DuplicateResourceFoundException;
import com.Suyash.StockFlow.exceptions.ResourceNotFoundException;
import com.Suyash.StockFlow.model.Category;
import com.Suyash.StockFlow.model.Product;
import com.Suyash.StockFlow.payload.mapper.ProductMapper;
import com.Suyash.StockFlow.payload.request.ProductDto;
import com.Suyash.StockFlow.payload.response.pageResponse.ProductPageResponse;
import com.Suyash.StockFlow.payload.response.ProductResponse;
import com.Suyash.StockFlow.repository.CategoryRepository;
import com.Suyash.StockFlow.repository.ProductRepository;
import com.Suyash.StockFlow.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductMapper mapper;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public ProductResponse createProduct(ProductDto productDto) {

        Category category = categoryRepository.findById(productDto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category with categoryId: " + productDto.getCategoryId() + " not found"));

        Product product = mapper.toEntity(productDto);

        product.setCategory(category);
        product.setActive(true);

        Product savedProduct = productRepository.save(product);
        return mapper.toResponse(savedProduct);
    }

    @Override
    public ProductResponse getProductById(Long productId) {
        Product product = productRepository.findByProductIdAndActiveTrue(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product with productId: " + productId + " not found."));
        return mapper.toResponse(product);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductPageResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sort = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Product> productPage = productRepository.findByActiveTrue(pageable);

        List<ProductResponse> productResponses = productPage.getContent()
                .stream()
                .map(mapper::toResponse)
                .toList();

        return ProductPageResponse.builder()
                .content(productResponses)
                .pageNumber(productPage.getNumber())
                .pageSize(productPage.getSize())
                .totalElements(productPage.getTotalElements())
                .totalPages(productPage.getTotalPages())
                .lastPage(productPage.isLast())
                .build();
    }

    @Override
    public ProductResponse updateProduct(Long productId, ProductDto productDto) {

       Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product with productId: "+ productId + " not found."));

       productRepository.findBySlugIgnoreCase(productDto.getSlug())
               .ifPresent(matchedProduct -> {
                   if(!matchedProduct.getProductId().equals(productId)){
                       throw new DuplicateResourceFoundException("Product with slug '" + productDto.getSlug() + "' already exists");
                   }
               });

       Category category = categoryRepository.findById(productDto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category with categoryId: " + productDto.getCategoryId() + " not found."));

       product.setProductName(productDto.getProductName());
       product.setSlug(productDto.getSlug());
       product.setDescription(productDto.getDescription());
       product.setBrand(productDto.getSlug());
       product.setImageUrl(productDto.getImageUrl());
       product.setCategory(category);

       Product updatedProduct = productRepository.save(product);
       return mapper.toResponse(updatedProduct);
    }

    @Override
    public String deleteProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product with productId: " + productId + " not found"));

        product.setActive(false);
        productRepository.save(product);
        return "Product with productId: " + productId + " deactivated successfully";
    }

    @Override
    @Transactional(readOnly = true)
    public ProductPageResponse getAllProductsByCategory(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder, Long categoryId) {

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Category with categoryId: " + categoryId + " not found"
                ));

        Sort sort = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Product> productPage = productRepository.findByCategoryAndActiveTrue(category, pageable);

        List<ProductResponse> productResponses = productPage.getContent()
                .stream()
                .map(mapper::toResponse)
                .toList();

        return ProductPageResponse.builder()
                .content(productResponses)
                .pageNumber(productPage.getNumber())
                .pageSize(productPage.getSize())
                .totalElements(productPage.getTotalElements())
                .totalPages(productPage.getTotalPages())
                .lastPage(productPage.isLast())
                .build();
    }
}