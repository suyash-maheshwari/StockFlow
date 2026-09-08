package com.Suyash.StockFlow.payload;

import com.Suyash.StockFlow.model.Product;
import com.Suyash.StockFlow.payload.request.ProductDto;
import com.Suyash.StockFlow.payload.response.ProductResponse;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {
    public ProductResponse toResponse(Product product){
        return ProductResponse.builder()
                .productId(product.getProductId())
                .productName(product.getProductName())
                .slug(product.getSlug())
                .description(product.getDescription())
                .brand(product.getBrand())
                .imageUrl(product.getImageUrl())
                .active(product.isActive())
                .categoryId(product.getCategory().getCategoryId())
                .categoryName(product.getCategory().getCategoryName())
                .build();
    }

    public Product toEntity(ProductDto productDto){
        Product product = new Product();
        product.setProductName(productDto.getProductName());
        product.setDescription(productDto.getDescription());
        product.setSlug(productDto.getSlug());
        product.setBrand(productDto.getBrand());
        product.setImageUrl(productDto.getImageUrl());

        return product;
    }
}
