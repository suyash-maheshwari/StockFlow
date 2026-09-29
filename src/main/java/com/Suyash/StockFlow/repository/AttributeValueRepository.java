package com.Suyash.StockFlow.repository;

import com.Suyash.StockFlow.model.Attribute;
import com.Suyash.StockFlow.model.AttributeValue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AttributeValueRepository extends JpaRepository<AttributeValue, Long> {
    List<AttributeValue> findByAttribute(Attribute attribute);

    Optional<AttributeValue> findByAttributeAndValueIgnoreCase(Attribute attribute, String trimmedName);
}
