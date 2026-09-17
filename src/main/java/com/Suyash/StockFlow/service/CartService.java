package com.Suyash.StockFlow.service;

import com.Suyash.StockFlow.payload.request.AddCartItemDto;
import com.Suyash.StockFlow.payload.request.UpdateCartItemDto;
import com.Suyash.StockFlow.payload.response.CartResponse;

public interface CartService {
    CartResponse getOrCreateCart(Long userId);

    CartResponse addItem(Long cartId, AddCartItemDto addCartItemDto);

    CartResponse updateItemQuantity(Long cartId, Long cartItemId, UpdateCartItemDto itemDto);

    CartResponse removeItem(Long cartId, Long cartItemId);

    String clearCart(Long cartId);
}
