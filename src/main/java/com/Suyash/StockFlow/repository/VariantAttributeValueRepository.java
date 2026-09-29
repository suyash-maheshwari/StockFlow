package com.Suyash.StockFlow.repository;

import com.Suyash.StockFlow.model.ProductVariant;
import com.Suyash.StockFlow.model.VariantAttributeValue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VariantAttributeValueRepository extends JpaRepository<VariantAttributeValue, Long> {
    Optional<VariantAttributeValue> findByProductVariantAndAttributeValue_AttributeAttributeId(ProductVariant variant, Long attributeId);

    List<VariantAttributeValue> findByProductVariant(ProductVariant variant);
}
