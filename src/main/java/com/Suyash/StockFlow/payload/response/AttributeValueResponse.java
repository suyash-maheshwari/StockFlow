package com.Suyash.StockFlow.payload.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttributeValueResponse {

    private Long attributeValueId;
    private Long attributeId;
    private String attributeName;
    private String value;
}
