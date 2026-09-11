package com.Suyash.StockFlow.payload.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductStockDto {

    @NotNull(message = "Product Variant Id is required")
    private Long productVariantId;

    @NotNull(message = "Warehouse Id is requires")
    private Long warehouseId;


    @NotNull(message = "Quantity is required")
    @PositiveOrZero(message = "Quantity cannot be negative")
    private Integer quantity;
}
