package com.Suyash.StockFlow.payload.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttributeValueDto {

    @NotBlank(message = "Value is required")
    private String value;
}
