package com.Suyash.StockFlow.controller;

import com.Suyash.StockFlow.config.AppConstants;
import com.Suyash.StockFlow.payload.request.SupplierDto;
import com.Suyash.StockFlow.payload.response.SupplierResponse;
import com.Suyash.StockFlow.payload.response.pageResponse.SupplierPageResponse;
import com.Suyash.StockFlow.service.SupplierService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/suppliers")
@Validated
public class SupplierController {

    @Autowired
    private SupplierService supplierService;

    @PostMapping
    public ResponseEntity<SupplierResponse> createSupplier(@Valid @RequestBody SupplierDto supplierDto){
        SupplierResponse response = supplierService.createSupplier(supplierDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{supplierId})")
    public ResponseEntity<SupplierResponse> getSupplierById(@PathVariable @Min(1) Long supplierId){
        SupplierResponse response = supplierService.getSupplierById(supplierId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<SupplierPageResponse> getAllSuppliers(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER) @Min(0) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE) @Min(1) @Max(100) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_SUPPLIER_BY) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR) String sortOrder
    ){
        SupplierPageResponse response = supplierService.getAllSuppliers(pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{supplierId}")
    public ResponseEntity<SupplierResponse> updateSupplier(@PathVariable @Min(1) Long supplierId,
                                                           @RequestBody SupplierDto supplierDto){
        SupplierResponse response = supplierService.updateSupplier(supplierId, supplierDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{supplierId}")
    public ResponseEntity<String> deactivateSupplier(@PathVariable @Min(1) Long supplierId){
        String message = supplierService.deactivateSupplier(supplierId);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}
