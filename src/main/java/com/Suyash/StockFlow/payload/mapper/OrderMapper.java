package com.Suyash.StockFlow.payload.mapper;

import com.Suyash.StockFlow.model.Order;
import com.Suyash.StockFlow.model.OrderItem;
import com.Suyash.StockFlow.payload.response.OrderItemResponse;
import com.Suyash.StockFlow.payload.response.OrderResponse;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

    public OrderItemResponse toItemResponse(OrderItem item){
        return OrderItemResponse.builder()
                .orderItemId(item.getOrderItemId())
                .productVariantId(item.getProductVariant().getVariantId())
                .variantName(item.getProductVariant().getVariantName())
                .sku(item.getProductVariant().getSku())
                .quantity(item.getQuantity())
                .priceAtPurchase(item.getPriceAtPurchase())
                .subtotal(item.getSubtotal())
                .build();
    }

    public OrderResponse toResponse(Order order){
        return OrderResponse.builder()
                .orderId(order.getOrderId())
                .userId(order.getUser().getId())
                .status(order.getStatus())
                .shippingAddressLine(order.getShippingAddressLine())
                .shippingCity(order.getShippingCity())
                .shippingState(order.getShippingState())
                .shippingPostalCode(order.getShippingPostalCode())
                .shippingCountry(order.getShippingCountry())
                .totalAmount(order.getTotalAmount())
                .orderDate(order.getOrderDate())
                .items(order.getItems().stream().map(this::toItemResponse).toList())
                .build();
    }
}
