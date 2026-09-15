package com.Suyash.StockFlow.payload.mapper;

import com.Suyash.StockFlow.model.PurchaseOrder;
import com.Suyash.StockFlow.model.PurchaseOrderItem;
import com.Suyash.StockFlow.payload.response.PurchaseOrderItemResponse;
import com.Suyash.StockFlow.payload.response.PurchaseOrderResponse;
import org.springframework.stereotype.Component;

@Component
public class PurchaseOrderMapper {

    public PurchaseOrderItemResponse toItemResponse(PurchaseOrderItem item){
        return PurchaseOrderItemResponse.builder()
                .purchaseOrderItemId(item.getPurchaseOrderItemId())
                .productVariantId(item.getProductVariant().getVariantId())
                .variantName(item.getProductVariant().getVariantName())
                .sku(item.getProductVariant().getSku())
                .quantity(item.getQuantity())
                .unitCost(item.getUnitCost())
                .build();
    }

    public PurchaseOrderResponse toResponse(PurchaseOrder order){
        return PurchaseOrderResponse.builder()
                .purchaseOrderId(order.getPurchaseOrderId())
                .supplierId(order.getSupplier().getSupplierId())
                .supplierName(order.getSupplier().getSupplierName())
                .warehouseId(order.getWarehouse().getWarehouseId())
                .warehouseName(order.getWarehouse().getWarehouseName())
                .status(order.getStatus())
                .orderDate(order.getOrderDate())
                .expectedDeliveryDate(order.getExpectedDeliveryDate())
                .items(order.getItems().stream().map(this :: toItemResponse).toList())
                .build();
    }

}
