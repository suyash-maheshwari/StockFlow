package com.Suyash.StockFlow.service.serviceImpl;

import com.Suyash.StockFlow.exceptions.InsufficientStockException;
import com.Suyash.StockFlow.exceptions.InvalidTransferException;
import com.Suyash.StockFlow.exceptions.ResourceNotFoundException;
import com.Suyash.StockFlow.model.ProductStock;
import com.Suyash.StockFlow.model.ProductVariant;
import com.Suyash.StockFlow.model.StockTransfer;
import com.Suyash.StockFlow.model.Warehouse;
import com.Suyash.StockFlow.payload.mapper.StockTransferMapper;
import com.Suyash.StockFlow.payload.request.StockTransferDto;
import com.Suyash.StockFlow.payload.response.StockTransferResponse;
import com.Suyash.StockFlow.payload.response.pageResponse.StockTransferPageResponse;
import com.Suyash.StockFlow.repository.ProductStockRepository;
import com.Suyash.StockFlow.repository.ProductVariantRepository;
import com.Suyash.StockFlow.repository.StockTransferRepository;
import com.Suyash.StockFlow.repository.WarehouseRepository;
import com.Suyash.StockFlow.service.StockTransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StockTransferServiceImpl implements StockTransferService {

    @Autowired
    private StockTransferRepository transferRepository;

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Autowired
    private ProductVariantRepository variantRepository;

    @Autowired
    private ProductStockRepository stockRepository;

    @Autowired
    private StockTransferMapper mapper;

    @Override
    @Transactional
    public StockTransferResponse executeTransfer(StockTransferDto dto) {

        if (dto.getSourceWarehouseId().equals(dto.getDestinationWarehouseId())){
            throw new InvalidTransferException(
                    "Source and Destination warehouse cannot be same"
            );
        }

        ProductVariant variant = variantRepository.findByVariantIdAndActiveTrue(dto.getProductVariantId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product Variant not found or inactive"
                ));

        Warehouse source = warehouseRepository.findByWarehouseIdAndActiveTrue(dto.getSourceWarehouseId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Source warehouse not found or inactive"
                ));

        Warehouse destination = warehouseRepository.findByWarehouseIdAndActiveTrue(dto.getDestinationWarehouseId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Destination warehouse not found or inactive"
                ));

        // Step 1: Find and decrement the stock

        ProductStock sourceStock = stockRepository.findByProductVariantAndWarehouse(variant, source)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No stock record found for this variant in source warehouse"
                ));

        if(sourceStock.getQuantity() < dto.getQuantity()){
            throw new InsufficientStockException(
                    "Can not transfer " + dto.getQuantity() + " - only " + sourceStock.getQuantity() + " available at source"
                );
        }

        sourceStock.setQuantity(sourceStock.getQuantity()- dto.getQuantity());
        stockRepository.save(sourceStock);

        // Step 2 : Find (or create) and Increment destination stock

        ProductStock destinationStock = stockRepository.findByProductVariantAndWarehouse(variant, destination)
                .orElse(null);

        if(destinationStock == null) {
            destinationStock = new ProductStock();
            destinationStock.setProductVariant(variant);
            destinationStock.setWarehouse(destination);
            destinationStock.setQuantity(0);
        }

        destinationStock.setQuantity(destinationStock.getQuantity() + dto.getQuantity());
        stockRepository.save(destinationStock);

        // Step 3: record the transfer quantity

        StockTransfer transfer = new StockTransfer();
        transfer.setProductVariant(variant);
        transfer.setSourceWarehouse(source);
        transfer.setDestinationWarehouse(destination);
        transfer.setQuantity(dto.getQuantity());

        StockTransfer saved = transferRepository.save(transfer);
        return mapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public StockTransferResponse getTransferById(Long transferId) {

        StockTransfer transfer = transferRepository.findById(transferId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Stock Transfer with transferId: " + transferId + " not found"
                ));

        return mapper.toResponse(transfer);
    }

    @Override
    @Transactional(readOnly = true)
    public StockTransferPageResponse getAllTransfers(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {

        Sort sort = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<StockTransfer> transferPage = transferRepository.findAll(pageable);

        List<StockTransferResponse> responses = transferPage.getContent()
                .stream()
                .map(mapper::toResponse)
                .toList();

        return StockTransferPageResponse.builder()
                .content(responses)
                .pageNumber(transferPage.getNumber())
                .pageSize(transferPage.getSize())
                .totalElements(transferPage.getTotalElements())
                .totalPages(transferPage.getTotalPages())
                .lastPage(transferPage.isLast())
                .build();
    }

}