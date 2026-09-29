package com.Suyash.StockFlow.payload.response;

import lombok.*;

import java.util.Map;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VariantAttributeSummary {

    private Long productVariantId;
    private Map<String, String> attributes;
}
