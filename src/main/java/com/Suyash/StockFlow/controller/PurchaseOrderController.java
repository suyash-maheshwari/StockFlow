package com.Suyash.StockFlow.controller;

import com.Suyash.StockFlow.config.AppConstants;
import com.Suyash.StockFlow.payload.request.PurchaseOrderDto;
import com.Suyash.StockFlow.payload.request.PurchaseOrderStatusUpdateDto;
import com.Suyash.StockFlow.payload.response.PurchaseOrderResponse;
import com.Suyash.StockFlow.payload.response.pageResponse.PurchaseOrderPageResponse;
import com.Suyash.StockFlow.service.PurchaseOrderService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/purchase-orders")
@Validated
public class PurchaseOrderController {

    @Autowired
    private PurchaseOrderService orderService;

    @PostMapping
    public ResponseEntity<PurchaseOrderResponse> createPurchaseOrder(@Valid @RequestBody PurchaseOrderDto dto){
        PurchaseOrderResponse response = orderService.createPurchaseOrder(dto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{purchaseOrderId}")
    public ResponseEntity<PurchaseOrderResponse> getPurchaseOrderById(@PathVariable @Min(1) Long purchaseOrderId){
        PurchaseOrderResponse response = orderService.getPurchaseOrderById(purchaseOrderId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<PurchaseOrderPageResponse> getAllPurchaseOrders(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER) @Min(0) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE) @Min(1) @Max(100) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_PURCHASE_ORDER_BY) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR) String sortOrder
    ){
        PurchaseOrderPageResponse response = orderService.getAllPurchaseOrders(pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/supplier/{supplierId}")
    public ResponseEntity<PurchaseOrderPageResponse> getPurchaseOrdersBySupplier(
            @PathVariable @Min(1) Long supplierId,
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER) @Min(0) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE) @Min(1) @Max(100) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_PURCHASE_ORDER_BY) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR) String sortOrder
    ){
        PurchaseOrderPageResponse response = orderService.getPurchaseOrdersBySupplier(pageNumber, pageSize, sortBy, sortOrder, supplierId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("/{purchaseOrderId}/status")
    public ResponseEntity<PurchaseOrderResponse> updateStatus(@PathVariable @Min(1) Long purchaseOrderId, @Valid @RequestBody PurchaseOrderStatusUpdateDto statusUpdateDto){
        PurchaseOrderResponse response = orderService.updateStatus(purchaseOrderId, statusUpdateDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
