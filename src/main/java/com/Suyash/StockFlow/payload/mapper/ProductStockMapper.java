package com.Suyash.StockFlow.payload.mapper;

import com.Suyash.StockFlow.model.ProductStock;
import com.Suyash.StockFlow.payload.response.ProductStockResponse;
import org.springframework.stereotype.Component;

@Component
public class ProductStockMapper {

    public ProductStockResponse toResponse(ProductStock stock){

        return ProductStockResponse.builder()
                .stockId(stock.getStockId())
                .productVariantId(stock.getProductVariant().getVariantId())
                .variantName(stock.getProductVariant().getVariantName())
                .sku(stock.getProductVariant().getSku())
                .warehouseId(stock.getWarehouse().getWarehouseId())
                .warehouseName(stock.getWarehouse().getWarehouseName())
                .quantity(stock.getQuantity())
                .build();
    }
    // No toEntity() here — unlike other modules, BOTH relationships
    // (variant AND warehouse) need DB lookups, so entity construction happens entirely in
    // the service layer this time, not via a mapper method.
}
