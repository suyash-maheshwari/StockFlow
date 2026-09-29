package com.Suyash.StockFlow.payload.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssignAttributeValueDto {

    @NotNull(message = "Attribute Value Id is required")
    private Long attributeValueId;
}
