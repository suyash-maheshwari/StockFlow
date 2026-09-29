package com.Suyash.StockFlow.controller;

import com.Suyash.StockFlow.config.AppConstants;
import com.Suyash.StockFlow.payload.request.AssignAttributeValueDto;
import com.Suyash.StockFlow.payload.request.BulkProductVariantRequest;
import com.Suyash.StockFlow.payload.request.ProductVariantDto;
import com.Suyash.StockFlow.payload.response.ProductVariantResponse;
import com.Suyash.StockFlow.payload.response.VariantAttributeSummary;
import com.Suyash.StockFlow.payload.response.pageResponse.BulkProductVariantResult;
import com.Suyash.StockFlow.payload.response.pageResponse.ProductVariantPageResponse;
import com.Suyash.StockFlow.service.ProductVariantService;
import com.Suyash.StockFlow.service.VariantAttributeService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/product-variants")
public class ProductVariantController {

    @Autowired
    private ProductVariantService variantService;

    @Autowired
    private VariantAttributeService attributeService;

    @PostMapping
    public ResponseEntity<ProductVariantResponse> createProductVariant(@Valid @RequestBody ProductVariantDto variantDto){
        ProductVariantResponse response = variantService.createProductVariant(variantDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{variantId}")
    public ResponseEntity<ProductVariantResponse> getProductVariantById(@PathVariable @Min(1) Long variantId){
        ProductVariantResponse response = variantService.getProductVariantById(variantId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<ProductVariantPageResponse> getAllProductVariants(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER) @Min(0) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE)@Min(1) @Max(100) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_PRODUCT_VARIANTS_BY) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR) String sortOrder
    ){
        ProductVariantPageResponse response = variantService.getAllProductVariants(pageNumber,pageSize,sortBy,sortOrder);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping("/product/{productId}")
    public ResponseEntity<ProductVariantPageResponse> getAllVariantsByProduct(
            @PathVariable @Min(1) Long productId,
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER)@Min(0) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE)@Min(1) @Max(100) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_PRODUCT_VARIANTS_BY) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR) String sortOrder
    ){
        ProductVariantPageResponse response = variantService.getAllVariantsByProduct(pageNumber, pageSize, sortBy, sortOrder, productId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{variantId}")
    public ResponseEntity<ProductVariantResponse> updateProductVariant(@Valid @RequestBody ProductVariantDto dto, @PathVariable @Min(1) Long variantId){
        ProductVariantResponse response = variantService.updateProductVariant(dto, variantId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{variantId}")
    public ResponseEntity<String> deactivateProductVariant(@PathVariable @Min(1) Long variantId){
        String response = variantService.deactivateProductVariant(variantId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/bulk")
    public ResponseEntity<BulkProductVariantResult> createProductVariantsBulk(@RequestBody BulkProductVariantRequest request){
        BulkProductVariantResult result = variantService.createProductVariantsBulk(request);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @PostMapping("/{variantId}/attributes")
    public ResponseEntity<VariantAttributeSummary> assignAttribute(@PathVariable @Min(1) Long variantId,
                                                                   @Valid @RequestBody AssignAttributeValueDto dto){
        VariantAttributeSummary summary = attributeService.assignAttribute(variantId, dto);
        return new ResponseEntity<>(summary, HttpStatus.CREATED);
    }

    @GetMapping("/{variantId}/attributes")
    public ResponseEntity<VariantAttributeSummary> getAttributesForVariant(@PathVariable @Min(1) Long variantId){
        VariantAttributeSummary summary = attributeService.getAttributesForVariant(variantId);
        return new ResponseEntity<>(summary, HttpStatus.OK);
    }

    @DeleteMapping("/{variantId}/attributes/{attributeId}")
    public ResponseEntity<String> removeAttribute(@PathVariable @Min(1) Long variantId, @PathVariable @Min(1) Long attributeId){
        String message = attributeService.removeAttribute(variantId, attributeId);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}

