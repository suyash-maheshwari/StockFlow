package com.Suyash.StockFlow.repository;

import com.Suyash.StockFlow.model.Warehouse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {
    Optional<Warehouse> findByWarehouseNameIgnoreCase(String warehouseName);

    Optional<Warehouse> findByWarehouseIdAndActiveTrue(Long warehouseId);

    Page<Warehouse> findByActiveTrue(Pageable pageable);
}
