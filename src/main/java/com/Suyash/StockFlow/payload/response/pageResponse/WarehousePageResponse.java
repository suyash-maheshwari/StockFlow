package com.Suyash.StockFlow.payload.response.pageResponse;

import com.Suyash.StockFlow.payload.response.WarehouseResponse;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WarehousePageResponse {

    private List<WarehouseResponse> content;

    private Integer pageNumber;
    private Integer pageSize;
    private Long totalElements;
    private Integer totalPages;
    private boolean lastPage;
}
