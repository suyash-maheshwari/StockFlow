package com.Suyash.StockFlow.payload.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttributeDto {

    @NotBlank(message = "Attribute Name is required")
    private String attributeName;
}
