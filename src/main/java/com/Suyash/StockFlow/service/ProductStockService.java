package com.Suyash.StockFlow.service;

import com.Suyash.StockFlow.payload.request.ProductStockDto;
import com.Suyash.StockFlow.payload.request.StockAdjustmentRequest;
import com.Suyash.StockFlow.payload.response.ProductStockResponse;
import com.Suyash.StockFlow.payload.response.pageResponse.ProductStockPageResponse;

public interface ProductStockService {
    ProductStockResponse createStock(ProductStockDto stockDto);

    ProductStockResponse getStockById(Long stockId);

    ProductStockPageResponse getAllStocks(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    ProductStockPageResponse getStocksByVariant(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder, Long variantId);

    ProductStockPageResponse getStocksByWarehouse(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder, Long warehouseId);

    ProductStockResponse adjustStock(Long stockId, StockAdjustmentRequest request);

    String deleteStock(Long stockId);
}
