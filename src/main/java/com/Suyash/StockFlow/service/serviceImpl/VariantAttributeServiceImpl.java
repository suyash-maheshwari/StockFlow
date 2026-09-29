package com.Suyash.StockFlow.service.serviceImpl;

import com.Suyash.StockFlow.exceptions.DuplicateResourceFoundException;
import com.Suyash.StockFlow.exceptions.ResourceNotFoundException;
import com.Suyash.StockFlow.model.AttributeValue;
import com.Suyash.StockFlow.model.ProductVariant;
import com.Suyash.StockFlow.model.VariantAttributeValue;
import com.Suyash.StockFlow.payload.request.AssignAttributeValueDto;
import com.Suyash.StockFlow.payload.response.VariantAttributeSummary;
import com.Suyash.StockFlow.repository.AttributeValueRepository;
import com.Suyash.StockFlow.repository.ProductVariantRepository;
import com.Suyash.StockFlow.repository.VariantAttributeValueRepository;
import com.Suyash.StockFlow.service.VariantAttributeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class VariantAttributeServiceImpl implements VariantAttributeService {

    @Autowired
    private VariantAttributeValueRepository variantAttributeValueRepository;

    @Autowired
    private AttributeValueRepository attributeValueRepository;

    @Autowired
    private ProductVariantRepository variantRepository;

    @Override
    @Transactional
    public VariantAttributeSummary assignAttribute(Long variantId, AssignAttributeValueDto dto) {
        ProductVariant variant = variantRepository.findByVariantIdAndActiveTrue(variantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Variant with variantId: " + variantId + " not found or inactive"
                ));

        AttributeValue newValue = attributeValueRepository.findById(dto.getAttributeValueId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Attribute Value with attributeValue: " + dto.getAttributeValueId() + " not found"
                ));

        variantAttributeValueRepository.findByProductVariantAndAttributeValue_AttributeAttributeId(variant, newValue.getAttribute().getAttributeId())
                .ifPresent(existingAssignment -> {
                    variantAttributeValueRepository.delete(existingAssignment);
                });

        VariantAttributeValue assignment = new VariantAttributeValue();
        assignment.setAttributeValue(newValue);
        assignment.setProductVariant(variant);
        variantAttributeValueRepository.save(assignment);
        return getAttributesForVariant(variantId);
    }

    @Override
    public VariantAttributeSummary getAttributesForVariant(Long variantId) {

        ProductVariant variant = variantRepository.findByVariantIdAndActiveTrue(variantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Variant with variantId: " + variantId + " not found or inactive"
                ));

        List<VariantAttributeValue> assignments = variantAttributeValueRepository.findByProductVariant(variant);

        Map<String, String> attributeMap = new HashMap<>();
        for (VariantAttributeValue a : assignments) {
            attributeMap.put(a.getAttributeValue().getAttribute().getAttributeName(), a.getAttributeValue().getValue());
        }

        return VariantAttributeSummary.builder()
                .productVariantId(variantId)
                .attributes(attributeMap)
                .build();
        }

    @Override
    public String removeAttribute(Long variantId, Long attributeId) {

        ProductVariant variant = variantRepository.findByVariantIdAndActiveTrue(variantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Variant with variantId: " + variantId + " not found or inactive"
                ));

        VariantAttributeValue assignment = variantAttributeValueRepository.findByProductVariantAndAttributeValue_AttributeAttributeId(variant, attributeId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "This variant has no value assigned for this attribute"
                ));

        variantAttributeValueRepository.delete(assignment);
        return "Attribute removed from variant successfully";
    }
}