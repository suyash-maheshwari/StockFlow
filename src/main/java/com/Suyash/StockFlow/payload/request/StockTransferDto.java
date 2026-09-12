package com.Suyash.StockFlow.payload.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockTransferDto {

    @NotNull(message = "Product Variant Id is required")
    private Long productVariantId;

    @NotNull(message = "Source Warehouse Id is required")
    private Long sourceWarehouseId;

    @NotNull(message = "Destination Warehouse Id is required")
    private Long destinationWarehouseId;

    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be greater than zero")
    private Integer quantity;
}
