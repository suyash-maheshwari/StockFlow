package com.Suyash.StockFlow.payload.response;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductVariantResponse {

    private Long variantId;
    private String variantName;
    private String sku;
    private BigDecimal price;
    private BigDecimal discountPrice;
    private Integer quantityAvailable;
    private boolean active;
    private Long productId;
    private String productName;
}
