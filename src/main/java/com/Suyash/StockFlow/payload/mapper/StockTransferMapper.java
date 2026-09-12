package com.Suyash.StockFlow.payload.mapper;

import com.Suyash.StockFlow.model.StockTransfer;
import com.Suyash.StockFlow.payload.request.StockTransferDto;
import com.Suyash.StockFlow.payload.response.StockTransferResponse;
import org.springframework.stereotype.Component;

@Component
public class StockTransferMapper {

    public StockTransferResponse toResponse(StockTransfer stockTransfer){
        return StockTransferResponse.builder()
                .transferId(stockTransfer.getTransferId())
                .productVariantId(stockTransfer.getProductVariant().getVariantId())
                .variantName(stockTransfer.getProductVariant().getVariantName())
                .sku(stockTransfer.getProductVariant().getSku())
                .sourceWarehouseName(stockTransfer.getSourceWarehouse().getWarehouseName())
                .destinationWarehouseName(stockTransfer.getDestinationWarehouse().getWarehouseName())
                .quantity(stockTransfer.getQuantity())
                .createdAt(stockTransfer.getCreatedAt())
                .build();
    }
}
