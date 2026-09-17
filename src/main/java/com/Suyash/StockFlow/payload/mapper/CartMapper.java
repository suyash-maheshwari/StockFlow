package com.Suyash.StockFlow.payload.mapper;

import com.Suyash.StockFlow.model.Cart;
import com.Suyash.StockFlow.model.CartItem;
import com.Suyash.StockFlow.payload.response.CartItemResponse;
import com.Suyash.StockFlow.payload.response.CartResponse;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class CartMapper {

    public CartItemResponse toItemResponse(CartItem item){

        BigDecimal price = item.getProductVariant().getPrice();
        BigDecimal discountPrice = item.getProductVariant().getDiscountPrice();
        BigDecimal effectivePrice = (discountPrice != null) ? discountPrice : price;
        BigDecimal subtotal = effectivePrice.multiply(BigDecimal.valueOf(item.getQuantity()));

        return CartItemResponse.builder()
                .cartItemId(item.getCartItemId())
                .productVariantId(item.getProductVariant().getVariantId())
                .variantName(item.getProductVariant().getVariantName())
                .sku(item.getProductVariant().getSku())
                .price(item.getProductVariant().getPrice())
                .discountPrice(discountPrice)
                .quantity(item.getQuantity())
                .subtotal(subtotal)
                .build();
    }

    public CartResponse toResponse(Cart cart){
        List<CartItemResponse> itemResponses = cart.getItems().stream().map(this::toItemResponse).toList();

        BigDecimal total = itemResponses.stream()
                .map(CartItemResponse::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return CartResponse.builder()
                .cartId(cart.getCartId())
                .userId(cart.getUser().getId())
                .items(itemResponses)
                .totalAmount(total)
                .build();
    }
}
