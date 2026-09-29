package com.Suyash.StockFlow.service.serviceImpl;

import com.Suyash.StockFlow.exceptions.DuplicateResourceFoundException;
import com.Suyash.StockFlow.exceptions.InvalidOrderException;
import com.Suyash.StockFlow.exceptions.ResourceNotFoundException;
import com.Suyash.StockFlow.model.Attribute;
import com.Suyash.StockFlow.model.AttributeValue;
import com.Suyash.StockFlow.payload.mapper.AttributeMapper;
import com.Suyash.StockFlow.payload.request.AttributeDto;
import com.Suyash.StockFlow.payload.request.AttributeValueDto;
import com.Suyash.StockFlow.payload.response.AttributeResponse;
import com.Suyash.StockFlow.payload.response.AttributeValueResponse;
import com.Suyash.StockFlow.repository.AttributeRepository;
import com.Suyash.StockFlow.repository.AttributeValueRepository;
import com.Suyash.StockFlow.repository.VariantAttributeValueRepository;
import com.Suyash.StockFlow.service.AttributeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AttributeServiceImpl implements AttributeService {

    @Autowired
    private AttributeRepository attributeRepository;

    @Autowired
    private AttributeValueRepository attributeValueRepository;

    @Autowired
    private VariantAttributeValueRepository variantAttributeValueRepository;

    @Autowired
    private AttributeMapper mapper;



    @Override
    public AttributeResponse createAttribute(AttributeDto dto) {

        String trimmedName = dto.getAttributeName().trim();
        attributeRepository.findByAttributeNameIgnoreCase(trimmedName)
                .ifPresent(existing -> {
                    throw new DuplicateResourceFoundException("Attribute '" + trimmedName + "' already exists.");
                });

        Attribute attribute = new Attribute();
        attribute.setAttributeName(trimmedName);
        Attribute saved = attributeRepository.save(attribute);
        return mapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AttributeResponse> getAllAttributes() {
        return attributeRepository.findAll().stream()
                .map(mapper::toResponse).toList();
    }

    @Override
    public String deleteAttribute(Long attributeId) {
        Attribute attribute = attributeRepository.findById(attributeId)
                .orElseThrow(()-> new ResourceNotFoundException(
                        "Attribute with attributeId: " + attributeId + " not found"
                ));

        List<AttributeValue> values = attributeValueRepository.findByAttribute(attribute);
        if(!values.isEmpty()){
            throw new InvalidOrderException("Cannot delete attribute — " + values.size() + " value(s) still exist under it. Delete those first.");
        }

        attributeRepository.deleteById(attributeId);
        return "Attribute with attributeId: " + attributeId + " deleted successfully";
    }

    @Override
    public AttributeValueResponse addValue(Long attributeId, AttributeValueDto dto) {
        Attribute attribute = attributeRepository.findById(attributeId)
                .orElseThrow(()-> new ResourceNotFoundException(
                        "Attribute with attributeId: " + attributeId + " not found"
                ));

        String trimmedValue = dto.getValue().trim();
        attributeValueRepository.findByAttributeAndValueIgnoreCase(attribute, trimmedValue)
                .ifPresent(existing -> {
                    throw new DuplicateResourceFoundException(
                             "Value '" + trimmedValue + "' already exists under attribute '" + attribute.getAttributeName() + "'."
                    );
                });

        AttributeValue value = new AttributeValue();
        value.setValue(trimmedValue);
        value.setAttribute(attribute);
        AttributeValue saved = attributeValueRepository.save(value);
        return mapper.toValueResponse(saved);
    }

    @Override
    public List<AttributeValueResponse> getAllValuesForAttribute(Long attributeId) {
        Attribute attribute = attributeRepository.findById(attributeId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Attribute with attributeId: " + attributeId + " not found"
                ));
        return attributeValueRepository.findByAttribute(attribute).stream().
                map(mapper::toValueResponse).toList();
    }

    @Override
    public String deleteValue(Long attributeValueId) {

        AttributeValue value = attributeValueRepository.findById(attributeValueId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Attribute Value with attributeValueId " + attributeValueId + " not found"
                ));

        attributeValueRepository.deleteById(attributeValueId);
        return "Attribute Value with attributeValueId " + attributeValueId + " deleted successfully";
    }


}
