package com.Suyash.StockFlow.controller;

import com.Suyash.StockFlow.payload.request.AttributeDto;
import com.Suyash.StockFlow.payload.request.AttributeValueDto;
import com.Suyash.StockFlow.payload.response.AttributeResponse;
import com.Suyash.StockFlow.payload.response.AttributeValueResponse;
import com.Suyash.StockFlow.service.AttributeService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/attributes")
@Validated
public class AttributeController {

    @Autowired
    private AttributeService attributeService;

    @PostMapping
    public ResponseEntity<AttributeResponse> createAttribute(@Valid @RequestBody AttributeDto dto){
        AttributeResponse response = attributeService.createAttribute(dto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<AttributeResponse>> getAllAttributes(){
        List<AttributeResponse> responses = attributeService.getAllAttributes();
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    @DeleteMapping("/{attributeId}")
    public ResponseEntity<String> deleteAttribute(@PathVariable Long attributeId){
        String message = attributeService.deleteAttribute(attributeId);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @PostMapping("/{attributeId}/values")
    public ResponseEntity<AttributeValueResponse> addValue(@Valid @RequestBody AttributeValueDto dto, @PathVariable @Min(1) Long attributeId){
        AttributeValueResponse response = attributeService.addValue(attributeId, dto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{attributeId}/values")
    public ResponseEntity<List<AttributeValueResponse>> getAllValuesForAttribute(@PathVariable Long attributeId){
        List<AttributeValueResponse> responses = attributeService.getAllValuesForAttribute(attributeId);
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }
}
