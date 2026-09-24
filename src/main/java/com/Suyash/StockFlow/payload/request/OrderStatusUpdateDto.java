package com.Suyash.StockFlow.payload.request;

import com.Suyash.StockFlow.enums.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderStatusUpdateDto {

    @NotNull(message = "Status is required")
    private OrderStatus status;
}
