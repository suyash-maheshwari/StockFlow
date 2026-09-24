package com.Suyash.StockFlow.controller;

import com.Suyash.StockFlow.config.AppConstants;
import com.Suyash.StockFlow.model.Order;
import com.Suyash.StockFlow.payload.request.OrderStatusUpdateDto;
import com.Suyash.StockFlow.payload.request.PlacedOrderDto;
import com.Suyash.StockFlow.payload.response.OrderResponse;
import com.Suyash.StockFlow.payload.response.pageResponse.OrderPageResponse;
import com.Suyash.StockFlow.service.OrderService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
@Validated
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/user/{userId}")
    public ResponseEntity<OrderResponse> placeOrder(@PathVariable @Min(1) Long userId, @Valid @RequestBody
        PlacedOrderDto dto){
        OrderResponse response = orderService.placeOrder(userId, dto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable @Min(1) Long orderId){
        OrderResponse response = orderService.getOrderById(orderId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<OrderPageResponse> getAllOrders(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER) @Min(0) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE) @Min(1) @Max(100) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_ORDER_BY) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR) String sortOrder
    ){
        OrderPageResponse response = orderService.getAllOrders(pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<OrderPageResponse> getOrdersForUser(
            @PathVariable @Min(1) Long userId,
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER) @Min(0) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE) @Min(1) @Max(100) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_ORDER_BY) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR) String sortOrder
    ){
        OrderPageResponse response = orderService.getOrdersForUser(pageNumber, pageSize, sortBy, sortOrder, userId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("/{orderId}/status")
    public ResponseEntity<OrderResponse> updateStatus(@PathVariable @Min(1) Long orderId, @RequestBody OrderStatusUpdateDto dto){
        OrderResponse response = orderService.updateStatus(orderId, dto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
