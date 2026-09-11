package com.Suyash.StockFlow.repository;

import com.Suyash.StockFlow.model.ProductStock;
import com.Suyash.StockFlow.model.StockTransaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockTransactionRepository extends JpaRepository<StockTransaction, Long> {
    Page<StockTransaction> findByProductStock(ProductStock stock, Pageable pageable);
}
