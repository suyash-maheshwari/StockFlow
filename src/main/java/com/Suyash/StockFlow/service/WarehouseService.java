package com.Suyash.StockFlow.service;

import com.Suyash.StockFlow.payload.request.WarehouseDto;
import com.Suyash.StockFlow.payload.response.WarehouseResponse;
import com.Suyash.StockFlow.payload.response.pageResponse.WarehousePageResponse;

public interface WarehouseService {
    WarehouseResponse createWarehouse(WarehouseDto warehouseDto);

    WarehouseResponse getWarehouseById(Long warehouseId);

    WarehousePageResponse getAllWarehouses(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    WarehouseResponse updateWarehouse(Long warehouseId, WarehouseDto warehouseDto);

    String deactivateWarehouse(Long warehouseId);
}
