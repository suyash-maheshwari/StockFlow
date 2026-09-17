package com.Suyash.StockFlow.repository;

import com.Suyash.StockFlow.model.Cart;
import com.Suyash.StockFlow.model.CartItem;
import com.Suyash.StockFlow.model.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByCartAndProductVariant(Cart cart, ProductVariant variant);
}
