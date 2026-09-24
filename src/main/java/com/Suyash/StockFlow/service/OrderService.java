package com.Suyash.StockFlow.service;

import com.Suyash.StockFlow.payload.request.OrderStatusUpdateDto;
import com.Suyash.StockFlow.payload.request.PlacedOrderDto;
import com.Suyash.StockFlow.payload.response.OrderResponse;
import com.Suyash.StockFlow.payload.response.pageResponse.OrderPageResponse;

public interface OrderService {
    OrderResponse placeOrder(Long userId, PlacedOrderDto dto);

    OrderResponse getOrderById(Long orderId);

    OrderPageResponse getAllOrders(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    OrderPageResponse getOrdersForUser(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder, Long userId);

    OrderResponse updateStatus(Long orderId, OrderStatusUpdateDto dto);
}
