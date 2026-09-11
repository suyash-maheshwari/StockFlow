package com.Suyash.StockFlow.controller;

import com.Suyash.StockFlow.config.AppConstants;
import com.Suyash.StockFlow.payload.request.ProductStockDto;
import com.Suyash.StockFlow.payload.request.StockAdjustmentRequest;
import com.Suyash.StockFlow.payload.response.ProductStockResponse;
import com.Suyash.StockFlow.payload.response.pageResponse.ProductStockPageResponse;
import com.Suyash.StockFlow.service.ProductStockService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/stocks")
@Validated
public class ProductStockController {

    @Autowired
    private ProductStockService stockService;

    @PostMapping
    public ResponseEntity<ProductStockResponse> createStock(@Valid @RequestBody ProductStockDto stockDto){
        ProductStockResponse response = stockService.createStock(stockDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{stockId}")
    public ResponseEntity<ProductStockResponse> getStockById(@PathVariable Long stockId){
        ProductStockResponse response = stockService.getStockById(stockId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<ProductStockPageResponse> getAllStocks(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER) @Min(0) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE) @Min(1) @Max(100) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_STOCK_BY) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR) String sortOrder
    ){
        ProductStockPageResponse response = stockService.getAllStocks(pageNumber,pageSize,sortBy,sortOrder);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/variant/{variantId}")
    public ResponseEntity<ProductStockPageResponse> getStocksByVariant(
            @PathVariable @Min(1) Long variantId,
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER) @Min(0) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE) @Min(1) @Max(100) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_STOCK_BY) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR) String sortOrder
    ){
        ProductStockPageResponse response = stockService.getStocksByVariant(pageNumber, pageSize, sortBy, sortOrder, variantId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/warehouse/{warehouseId}")
    public ResponseEntity<ProductStockPageResponse> getStocksByWarehouse(
            @PathVariable @Min(1) Long warehouseId,
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER) @Min(0) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE) @Min(1) @Max(100) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_STOCK_BY) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR) String sortOrder
    ){
        ProductStockPageResponse response = stockService.getStocksByWarehouse(pageNumber, pageSize, sortBy, sortOrder, warehouseId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("/{stockId}/adjust")
    public ResponseEntity<ProductStockResponse> adjustStock(@PathVariable @Min(1) Long stockId,@Valid @RequestBody StockAdjustmentRequest request){
        ProductStockResponse response = stockService.adjustStock(stockId,request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{stockId}")
    public ResponseEntity<String> deleteStock(@PathVariable@Min(1) Long stockId){
        String message = stockService.deleteStock(stockId);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}
