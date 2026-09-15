package com.Suyash.StockFlow.service;

import com.Suyash.StockFlow.payload.request.PurchaseOrderDto;
import com.Suyash.StockFlow.payload.request.PurchaseOrderStatusUpdateDto;
import com.Suyash.StockFlow.payload.response.PurchaseOrderResponse;
import com.Suyash.StockFlow.payload.response.pageResponse.PurchaseOrderPageResponse;

public interface PurchaseOrderService {
    PurchaseOrderResponse createPurchaseOrder(PurchaseOrderDto dto);

    PurchaseOrderResponse getPurchaseOrderById(Long purchaseOrderId);

    PurchaseOrderPageResponse getAllPurchaseOrders(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    PurchaseOrderPageResponse getPurchaseOrdersBySupplier(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder, Long supplierId);

    PurchaseOrderResponse updateStatus(Long purchaseOrderId, PurchaseOrderStatusUpdateDto statusUpdateDto);
}
