package com.Suyash.StockFlow.repository;

import com.Suyash.StockFlow.model.Attribute;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AttributeRepository extends JpaRepository<Attribute, Long> {
    Optional<Attribute> findByAttributeNameIgnoreCase(String trimmedName);
}
