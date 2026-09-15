package com.Suyash.StockFlow.repository;

import com.Suyash.StockFlow.model.PurchaseOrder;
import com.Suyash.StockFlow.model.Supplier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {
    Page<PurchaseOrder> findBySupplier(Supplier supplier, Pageable pageable);
}
