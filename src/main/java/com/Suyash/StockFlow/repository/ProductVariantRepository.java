package com.Suyash.StockFlow.repository;

import com.Suyash.StockFlow.model.Product;
import com.Suyash.StockFlow.model.ProductVariant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductVariantRepository extends JpaRepository<ProductVariant, Long> {
    Optional<ProductVariant> findBySkuIgnoreCase(String sku);

    Page<ProductVariant> findByActiveTrue(Pageable pageable);

    Page<ProductVariant> findByProductAndActiveTrue(Product product, Pageable pageable);

    Optional<ProductVariant> findByVariantIdAndActiveTrue(Long variantId);
}
