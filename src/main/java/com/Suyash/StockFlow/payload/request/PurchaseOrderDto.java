package com.Suyash.StockFlow.payload.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseOrderDto {

    @NotNull(message = "Supplier Id is required")
    private Long supplierId;

    @NotNull(message = "Warehouse Id is required")
    private Long warehouseId;

    private LocalDateTime expectedDeliveryDate;

    @NotEmpty(message = "At least one item is required")
    @Valid
    private List<PurchaseOrderItemDto> items;
}
