package com.Suyash.StockFlow.repository;

import com.Suyash.StockFlow.model.Cart;
import com.Suyash.StockFlow.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUser(User user);
}
