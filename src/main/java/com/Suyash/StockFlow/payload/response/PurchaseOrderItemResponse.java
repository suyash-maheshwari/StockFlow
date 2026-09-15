package com.Suyash.StockFlow.payload.response;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseOrderItemResponse {

    private Long purchaseOrderItemId;
    private Long productVariantId;
    private String variantName;
    private String sku;
    private Integer quantity;
    private BigDecimal unitCost;
}
