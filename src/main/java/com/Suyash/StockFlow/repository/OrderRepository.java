package com.Suyash.StockFlow.repository;

import com.Suyash.StockFlow.model.Order;
import com.Suyash.StockFlow.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Page<Order> findByUser(User user, Pageable pageable);
}
