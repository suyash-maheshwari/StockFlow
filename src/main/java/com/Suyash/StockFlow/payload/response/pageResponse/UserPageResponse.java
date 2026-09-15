package com.Suyash.StockFlow.payload.response.pageResponse;

import com.Suyash.StockFlow.payload.response.UserResponse;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPageResponse {

    private List<UserResponse> content;

    private Integer pageNumber;
    private Integer pageSize;
    private Long totalElements;
    private Integer totalPages;
    private boolean lastPage;
}
