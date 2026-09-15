package com.Suyash.StockFlow.payload.request;

import com.Suyash.StockFlow.enums.PurchaseOrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseOrderStatusUpdateDto {

    @NotNull(message = "Status is required")
    private PurchaseOrderStatus status;
}
