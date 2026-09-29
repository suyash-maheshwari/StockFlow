package com.Suyash.StockFlow.payload.mapper;

import com.Suyash.StockFlow.model.Attribute;
import com.Suyash.StockFlow.model.AttributeValue;
import com.Suyash.StockFlow.payload.response.AttributeResponse;
import com.Suyash.StockFlow.payload.response.AttributeValueResponse;
import org.springframework.stereotype.Component;

@Component
public class AttributeMapper {

    public AttributeResponse toResponse(Attribute attribute){
        return AttributeResponse.builder()
                .attributeId(attribute.getAttributeId())
                .attributeName(attribute.getAttributeName())
                .build();
    }

    public AttributeValueResponse toValueResponse(AttributeValue value){
        return AttributeValueResponse.builder()
                .attributeValueId(value.getAttributeValueId())
                .attributeId(value.getAttribute().getAttributeId())
                .attributeName(value.getAttribute().getAttributeName())
                .value(value.getValue())
                .build();
    }
}
