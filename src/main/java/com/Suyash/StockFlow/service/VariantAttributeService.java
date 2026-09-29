package com.Suyash.StockFlow.service;

import com.Suyash.StockFlow.payload.request.AssignAttributeValueDto;
import com.Suyash.StockFlow.payload.response.VariantAttributeSummary;

public interface VariantAttributeService {
    VariantAttributeSummary assignAttribute(Long variantId, AssignAttributeValueDto dto);

    VariantAttributeSummary getAttributesForVariant(Long variantId);

    String removeAttribute(Long variantId, Long attributeId);
}
