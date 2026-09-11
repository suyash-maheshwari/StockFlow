package com.Suyash.StockFlow.payload.response.pageResponse;

import com.Suyash.StockFlow.payload.response.ProductStockResponse;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductStockPageResponse {

    private List<ProductStockResponse> content;

    private Integer pageNumber;
    private Integer pageSize;
    private Long totalElements;
    private Integer totalPages;
    private boolean last;
}
