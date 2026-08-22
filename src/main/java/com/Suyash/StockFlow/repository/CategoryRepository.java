package com.Suyash.StockFlow.repository;

import com.Suyash.StockFlow.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByCategoryName(String categoryName);

    Optional<Category> findByCategoryNameIgnoreCase(String categoryName);
}
