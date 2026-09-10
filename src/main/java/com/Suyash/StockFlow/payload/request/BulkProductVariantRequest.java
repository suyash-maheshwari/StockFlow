package com.Suyash.StockFlow.payload.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BulkProductVariantRequest {

    @NotNull(message = "Product Id is required")
    private Long productId;

    @NotEmpty(message = "At least one variant is required")
    @Valid
    private List<ProductVariantItemDto> variants;
}
