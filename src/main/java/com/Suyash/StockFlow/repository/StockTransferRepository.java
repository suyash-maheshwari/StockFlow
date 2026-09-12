package com.Suyash.StockFlow.repository;

import com.Suyash.StockFlow.model.StockTransfer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockTransferRepository extends JpaRepository<StockTransfer, Long> {
}
