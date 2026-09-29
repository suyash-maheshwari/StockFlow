package com.Suyash.StockFlow.service;

import com.Suyash.StockFlow.payload.request.AttributeDto;
import com.Suyash.StockFlow.payload.request.AttributeValueDto;
import com.Suyash.StockFlow.payload.response.AttributeResponse;
import com.Suyash.StockFlow.payload.response.AttributeValueResponse;

import java.util.List;

public interface AttributeService {
    AttributeResponse createAttribute(AttributeDto dto);

    List<AttributeResponse> getAllAttributes();

    String deleteAttribute(Long attributeId);

    AttributeValueResponse addValue(Long attributeId, AttributeValueDto dto);

    List<AttributeValueResponse> getAllValuesForAttribute(Long attributeId);

    String deleteValue(Long attributeValueId);
}
