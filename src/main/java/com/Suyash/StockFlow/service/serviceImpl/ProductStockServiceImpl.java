package com.Suyash.StockFlow.service.serviceImpl;

import com.Suyash.StockFlow.exceptions.DuplicateResourceFoundException;
import com.Suyash.StockFlow.exceptions.InsufficientStockException;
import com.Suyash.StockFlow.exceptions.ResourceNotFoundException;
import com.Suyash.StockFlow.model.ProductStock;
import com.Suyash.StockFlow.model.ProductVariant;
import com.Suyash.StockFlow.model.Warehouse;
import com.Suyash.StockFlow.payload.mapper.ProductStockMapper;
import com.Suyash.StockFlow.payload.request.ProductStockDto;
import com.Suyash.StockFlow.payload.request.StockAdjustmentRequest;
import com.Suyash.StockFlow.payload.response.ProductStockResponse;
import com.Suyash.StockFlow.payload.response.pageResponse.ProductStockPageResponse;
import com.Suyash.StockFlow.repository.ProductStockRepository;
import com.Suyash.StockFlow.repository.ProductVariantRepository;
import com.Suyash.StockFlow.repository.WarehouseRepository;
import com.Suyash.StockFlow.service.ProductStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductStockServiceImpl implements ProductStockService {

    @Autowired
    private ProductStockRepository stockRepository;

    @Autowired
    private ProductStockMapper mapper;

    @Autowired
    private ProductVariantRepository variantRepository;

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Override
    public ProductStockResponse createStock(ProductStockDto stockDto) {

        ProductVariant productVariant = variantRepository.findByVariantIdAndActiveTrue(stockDto.getProductVariantId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product variant with variantId: " + stockDto.getProductVariantId() + " not found or inactive"
                ));

        Warehouse warehouse = warehouseRepository.findByWarehouseIdAndActiveTrue(stockDto.getWarehouseId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Warehouse with warehouseId: " + stockDto.getWarehouseId() + " not found or inactive"
                ));

        stockRepository.findByProductVariantAndWarehouse(productVariant, warehouse)
                .ifPresent(existing -> {
                    throw new DuplicateResourceFoundException(
                            "Stock record already exists for this variant at this warehouse. Use the adjust-quantity endpoint instead"
                    );
                });

        ProductStock stock = new ProductStock();
        stock.setProductVariant(productVariant);
        stock.setWarehouse(warehouse);
        stock.setQuantity(stockDto.getQuantity());

        ProductStock saved = stockRepository.save(stock);
        return mapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductStockResponse getStockById(Long stockId) {
        ProductStock stock = stockRepository.findById(stockId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Stock record with stockId: " + stockId + " not found"
                ));
        return mapper.toResponse(stock);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductStockPageResponse getAllStocks(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {

        Sort sort = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<ProductStock> stockPage = stockRepository.findAll(pageable);

        List<ProductStockResponse> responses = stockPage.getContent()
                .stream()
                .map(mapper::toResponse)
                .toList();

        return ProductStockPageResponse.builder()
                .content(responses)
                .pageNumber(stockPage.getNumber())
                .pageSize(stockPage.getSize())
                .totalElements(stockPage.getTotalElements())
                .totalPages(stockPage.getTotalPages())
                .last(stockPage.isLast())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public ProductStockPageResponse getStocksByVariant(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder, Long variantId) {

        ProductVariant variant = variantRepository.findByVariantIdAndActiveTrue(variantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product variant with variantId: " + variantId + " not found"
                ));

        Sort sort = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<ProductStock> stockPage = stockRepository.findByProductVariant(variant, pageable);

        List<ProductStockResponse> responses = stockPage.getContent()
                .stream()
                .map(mapper::toResponse)
                .toList();

        return ProductStockPageResponse.builder()
                .content(responses)
                .pageNumber(stockPage.getNumber())
                .pageSize(stockPage.getSize())
                .totalElements(stockPage.getTotalElements())
                .totalPages(stockPage.getTotalPages())
                .last(stockPage.isLast())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public ProductStockPageResponse getStocksByWarehouse(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder, Long warehouseId) {

        Warehouse warehouse = warehouseRepository.findByWarehouseIdAndActiveTrue(warehouseId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Warehouse with warehouseId: " + warehouseId + " not found"
                ));

        Sort sort = sortBy.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<ProductStock> stockPage= stockRepository.findByWarehouse(warehouse, pageable);

        List<ProductStockResponse> responses = stockPage.getContent()
                .stream()
                .map(mapper::toResponse)
                .toList();

        return ProductStockPageResponse.builder()
                .content(responses)
                .pageNumber(stockPage.getNumber())
                .pageSize(stockPage.getSize())
                .totalElements(stockPage.getTotalElements())
                .totalPages(stockPage.getTotalPages())
                .last(stockPage.isLast())
                .build();
    }

    @Override
    public ProductStockResponse adjustStock(Long stockId, StockAdjustmentRequest request) {
        ProductStock stock = stockRepository.findById(stockId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Stock record with stockId: " + stockId + " not found."
                ));

        int newQuantity = stock.getQuantity() + request.getQuantityChange();

        if(newQuantity < 0){
            throw new InsufficientStockException(
                    "Cannot reduce stock by " + Math.abs(request.getQuantityChange()) +
                            " — only " + stock.getQuantity() + " available."
            );
        }

        stock.setQuantity(newQuantity);
        ProductStock updated = stockRepository.save(stock);
        return mapper.toResponse(updated);
    }

    @Override
    public String deleteStock(Long stockId) {
        stockRepository.findById(stockId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Stock with stockId: " + stockId + " not found"
                ));

        stockRepository.deleteById(stockId);
        return "Stock record with stockId: " + stockId + " deleted successfully";
    }

}
