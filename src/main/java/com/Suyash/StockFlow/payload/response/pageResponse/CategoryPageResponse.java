package com.Suyash.StockFlow.payload.response.pageResponse;

import com.Suyash.StockFlow.payload.response.CategoryResponse;
import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryPageResponse {

    private List<CategoryResponse> content;

    private Integer pageNumber;
    private Integer pageSize;
    private Long totalElements;
    private Integer totalPages;
    private boolean lastPage;
}
