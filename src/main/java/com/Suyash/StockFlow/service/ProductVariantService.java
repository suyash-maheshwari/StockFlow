package com.Suyash.StockFlow.service;

import com.Suyash.StockFlow.payload.request.ProductVariantDto;
import com.Suyash.StockFlow.payload.response.ProductVariantResponse;
import com.Suyash.StockFlow.payload.response.pageResponse.ProductVariantPageResponse;

public interface ProductVariantService {
    ProductVariantResponse createProductVariant(ProductVariantDto variantDto);

    ProductVariantResponse getProductVariantById(Long variantId);

    ProductVariantPageResponse getAllProductVariants(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    ProductVariantPageResponse getAllVariantsByProduct(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder, Long productId);

    ProductVariantResponse updateProductVariant(ProductVariantDto dto, Long variantId);

    String deactivateProductVariant(Long variantId);
}
