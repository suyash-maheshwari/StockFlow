package com.Suyash.StockFlow.payload.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttributeResponse {

    private Long attributeId;
    private String attributeName;
}
