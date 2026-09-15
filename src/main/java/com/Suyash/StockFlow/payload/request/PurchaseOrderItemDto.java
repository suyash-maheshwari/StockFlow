package com.Suyash.StockFlow.payload.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseOrderItemDto {

    @NotNull(message = "Product Variant Id is required")
    private Long productVariantId;

    @NotNull(message = "Quantity id required")
    @Positive(message = "Quantity must be greater than zero")
    private Integer quantity;

    @NotNull(message = "Unit cost is required")
    @Positive(message = "Unit cost must be greater than zero")
    private BigDecimal unitCost;
}
