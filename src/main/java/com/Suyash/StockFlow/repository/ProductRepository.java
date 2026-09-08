package com.Suyash.StockFlow.repository;

import com.Suyash.StockFlow.model.Category;
import com.Suyash.StockFlow.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findBySlugIgnoreCase(String slug);
    Page<Product> findByActiveTrue(Pageable pageable);
    Optional<Product> findByProductIdAndActiveTrue(Long productId);

    Page<Product> findByCategoryAndActiveTrue(Category category, Pageable pageable);
}
