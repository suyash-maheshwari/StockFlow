package com.Suyash.StockFlow.payload.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlacedOrderDto {

    @NotNull(message = "Shipping Address Id is required")
    private Long shippingAddressId;
}
