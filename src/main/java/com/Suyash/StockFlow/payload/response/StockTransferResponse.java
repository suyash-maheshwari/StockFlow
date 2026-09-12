package com.Suyash.StockFlow.payload.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockTransferResponse {

    private Long transferId;
    private Long productVariantId;
    private String variantName;
    private String sku;
    private String sourceWarehouseName;
    private String destinationWarehouseName;
    private Integer quantity;
    private LocalDateTime createdAt;
}
