package com.Suyash.StockFlow.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDto {

    @NotBlank(message = "Product name is required")
    private String productName;

    @NotBlank(message = "Slug is required")
    private String slug;

    private String description;

    private String brand;

    private String imageUrl;

    @NotNull(message = "Category is required")
    private Long categoryId;
}
