package com.Suyash.StockFlow.repository;

import com.Suyash.StockFlow.model.ProductStock;
import com.Suyash.StockFlow.model.ProductVariant;
import com.Suyash.StockFlow.model.Warehouse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductStockRepository extends JpaRepository<ProductStock, Long> {
    Optional<ProductStock> findByProductVariantAndWarehouse(ProductVariant productVariant, Warehouse warehouse);

    Page<ProductStock> findByProductVariant(ProductVariant variant, Pageable pageable);

    Page<ProductStock> findByWarehouse(Warehouse warehouse, Pageable pageable);

    List<ProductStock> findByProductVariant(ProductVariant variant);

    @Query("SELECT COALESCE(SUM(ps.quantity),0) FROM ProductStock ps where ps.productVariant = :variant")
    Integer sumQuantityByProductVariant(@Param("variant") ProductVariant variant);
}
