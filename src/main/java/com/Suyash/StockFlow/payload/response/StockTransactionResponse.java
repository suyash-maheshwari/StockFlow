package com.Suyash.StockFlow.payload.response;

import com.Suyash.StockFlow.enums.TransactionType;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StockTransactionResponse {

    private Long transactionId;
    private Long stockId;
    private String variantName;
    private String sku;
    private String warehouseName;
    private Integer quantityChange;
    private Integer resultingQuantity;
    private TransactionType transactionType;
    private String note;
    private LocalDateTime createdAt;
}
