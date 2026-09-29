package com.Suyash.StockFlow.controller;

import com.Suyash.StockFlow.service.AttributeService;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/attribute-values")
@Validated
public class AttributeValueController {

    @Autowired
    private AttributeService service;

    @DeleteMapping("/{attributeValueId}")
    public ResponseEntity<String> deleteValue(@PathVariable @Min(1) Long attributeValueId){
        String message = service.deleteValue(attributeValueId);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}
