package com.Suyash.StockFlow.payload.response;

import com.Suyash.StockFlow.payload.request.CategoryDto;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponse {

    private Long categoryId;
    private String categoryName;
    private String description;

}
