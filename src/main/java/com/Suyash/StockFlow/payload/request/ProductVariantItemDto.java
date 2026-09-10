package com.Suyash.StockFlow.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductVariantItemDto {

    @NotBlank(message = "Variant Name is required")
    private String variantName;

    @NotBlank(message = "SKU is required")
    private String sku;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be greater than zero")
    private BigDecimal price;

    @PositiveOrZero(message = "Discount price cannot be negative")
    private BigDecimal discountPrice;

    @NotNull(message = "Quantity available is required")
    @PositiveOrZero(message = "Quantity available cannot be zero")
    private Integer quantityAvailable;

}
