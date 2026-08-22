package com.Suyash.StockFlow.payload.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BulkCategoryResult {
    private List<CategoryResponse> created;
    private List<String> skipped;
}
