package com.Suyash.StockFlow.payload.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductStockResponse {

    private Long stockId;
    private Long productVariantId;
    private String variantName;
    private String sku;
    private Long warehouseId;
    private String warehouseName;
    private Integer quantity;
}
