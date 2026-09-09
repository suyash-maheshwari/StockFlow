package com.Suyash.StockFlow.payload.mapper;

import com.Suyash.StockFlow.model.ProductVariant;
import com.Suyash.StockFlow.payload.request.ProductVariantDto;
import com.Suyash.StockFlow.payload.response.ProductVariantResponse;
import org.springframework.stereotype.Component;

@Component
public class ProductVariantMapper {

    public ProductVariantResponse toResponse(ProductVariant variant){
        return ProductVariantResponse.builder()
                .variantId(variant.getVariantId())
                .variantName(variant.getVariantName())
                .sku(variant.getSku())
                .price(variant.getPrice())
                .discountPrice(variant.getDiscountPrice())
                .quantityAvailable(variant.getQuantityAvailable())
                .active(variant.isActive())
                .productId(variant.getProduct().getProductId())
                .productName(variant.getProduct().getProductName())
                .build();
    }

    public ProductVariant toEntity(ProductVariantDto dto){
        ProductVariant variant = new ProductVariant();
        variant.setVariantName(dto.getVariantName());
        variant.setSku(dto.getSku());
        variant.setPrice(dto.getPrice());
        variant.setDiscountPrice(dto.getDiscountPrice());
        variant.setQuantityAvailable(dto.getQuantityAvailable());
        return variant;
    }
}
