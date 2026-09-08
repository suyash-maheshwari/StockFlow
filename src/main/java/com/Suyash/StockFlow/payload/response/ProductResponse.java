package com.Suyash.StockFlow.payload.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponse {

    private Long productId;
    private String productName;
    private String slug;
    private String description;
    private String brand;
    private String imageUrl;
    private boolean active;
    private Long categoryId;
    private String categoryName;
}
