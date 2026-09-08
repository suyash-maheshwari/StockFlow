package com.Suyash.StockFlow.service;

import com.Suyash.StockFlow.model.Product;
import com.Suyash.StockFlow.payload.request.ProductDto;
import com.Suyash.StockFlow.payload.response.ProductPageResponse;
import com.Suyash.StockFlow.payload.response.ProductResponse;

public interface ProductService {
    ProductResponse createProduct(ProductDto productDto);

    ProductResponse getProductById(Long productId);

    ProductPageResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    ProductResponse updateProduct(Long productId, ProductDto productDto);

    String deleteProduct(Long productId);

    ProductPageResponse getAllProductsByCategory(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder, Long categoryId);
}
