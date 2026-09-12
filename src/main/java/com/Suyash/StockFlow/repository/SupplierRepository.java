package com.Suyash.StockFlow.repository;

import com.Suyash.StockFlow.model.Supplier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SupplierRepository extends JpaRepository<Supplier, Long> {
    Optional<Supplier> findBySupplierNameIgnoreCase(String trimmedName);

    Optional<Supplier> findBySupplierIdAndActiveTrue(Long supplierId);

    Page<Supplier> findByActiveTrue(Pageable pageable);
}
