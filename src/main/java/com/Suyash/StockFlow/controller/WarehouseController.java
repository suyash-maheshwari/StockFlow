package com.Suyash.StockFlow.controller;

import com.Suyash.StockFlow.config.AppConstants;
import com.Suyash.StockFlow.payload.request.WarehouseDto;
import com.Suyash.StockFlow.payload.response.WarehouseResponse;
import com.Suyash.StockFlow.payload.response.pageResponse.WarehousePageResponse;
import com.Suyash.StockFlow.service.WarehouseService;
import jakarta.persistence.PreUpdate;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/warehouses")
public class WarehouseController {

    @Autowired
    private WarehouseService warehouseService;

    @PostMapping
    public ResponseEntity<WarehouseResponse> createWarehouse(@Valid @RequestBody WarehouseDto warehouseDto){
        WarehouseResponse response = warehouseService.createWarehouse(warehouseDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{warehouseId}")
    public ResponseEntity<WarehouseResponse> getWarehouseById(@PathVariable Long warehouseId){
        WarehouseResponse response = warehouseService.getWarehouseById(warehouseId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<WarehousePageResponse> getAllWarehouses(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER) @Min(0) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE) @Min(1) @Max(100) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_WAREHOUSE_BY ) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR) String sortOrder
    ){
        WarehousePageResponse response = warehouseService.getAllWarehouses(pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{warehouseId}")
    public ResponseEntity<WarehouseResponse> updateWarehouse(@Valid @PathVariable @Min(1) Long warehouseId, @RequestBody WarehouseDto warehouseDto){
        WarehouseResponse response = warehouseService.updateWarehouse(warehouseId, warehouseDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{warehouseId}")
    public ResponseEntity<String> deactivateWarehouse(@PathVariable @Min(1) Long warehouseId){
        String message = warehouseService.deactivateWarehouse(warehouseId);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}
