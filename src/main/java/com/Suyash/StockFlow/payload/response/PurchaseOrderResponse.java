package com.Suyash.StockFlow.payload.response;

import com.Suyash.StockFlow.enums.PurchaseOrderStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseOrderResponse {

    private Long purchaseOrderId;
    private Long supplierId;
    private String supplierName;
    private Long warehouseId;
    private String warehouseName;
    private PurchaseOrderStatus status;
    private LocalDateTime orderDate;
    private LocalDateTime expectedDeliveryDate;
    private List<PurchaseOrderItemResponse> items;
}
