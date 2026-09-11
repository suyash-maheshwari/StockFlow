package com.Suyash.StockFlow.payload.mapper;

import com.Suyash.StockFlow.model.StockTransaction;
import com.Suyash.StockFlow.payload.response.StockTransactionResponse;
import org.springframework.stereotype.Component;

@Component
public class StockTransactionMapper {

    public StockTransactionResponse toResponse(StockTransaction stockTransaction){
        return StockTransactionResponse.builder()
                .transactionId(stockTransaction.getTransactionId())
                .stockId(stockTransaction.getProductStock().getStockId())
                .variantName(stockTransaction.getProductStock().getProductVariant().getVariantName())
                .sku(stockTransaction.getProductStock().getProductVariant().getSku())
                .warehouseName(stockTransaction.getProductStock().getWarehouse().getWarehouseName())
                .quantityChange(stockTransaction.getQuantityChange())
                .resultingQuantity(stockTransaction.getResultingQuantity())
                .transactionType(stockTransaction.getTransactionType())
                .note(stockTransaction.getNote())
                .createdAt(stockTransaction.getCreatedAt())
                .build();
    }
}
