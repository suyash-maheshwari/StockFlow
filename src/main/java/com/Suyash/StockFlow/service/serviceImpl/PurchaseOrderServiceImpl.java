package com.Suyash.StockFlow.service.serviceImpl;

import com.Suyash.StockFlow.enums.PurchaseOrderStatus;
import com.Suyash.StockFlow.exceptions.InvalidStatusTransitionException;
import com.Suyash.StockFlow.exceptions.ResourceNotFoundException;
import com.Suyash.StockFlow.model.*;
import com.Suyash.StockFlow.payload.mapper.PurchaseOrderMapper;
import com.Suyash.StockFlow.payload.request.PurchaseOrderDto;
import com.Suyash.StockFlow.payload.request.PurchaseOrderItemDto;
import com.Suyash.StockFlow.payload.request.PurchaseOrderStatusUpdateDto;
import com.Suyash.StockFlow.payload.response.PurchaseOrderResponse;
import com.Suyash.StockFlow.payload.response.pageResponse.PurchaseOrderPageResponse;
import com.Suyash.StockFlow.repository.*;
import com.Suyash.StockFlow.service.PurchaseOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class PurchaseOrderServiceImpl implements PurchaseOrderService {

    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Autowired
    private ProductVariantRepository variantRepository;

    @Autowired
    private ProductStockRepository stockRepository;

    @Autowired
    private PurchaseOrderMapper mapper;

    @Override
    @Transactional
    public PurchaseOrderResponse createPurchaseOrder(PurchaseOrderDto dto) {

        Supplier supplier = supplierRepository.findBySupplierIdAndActiveTrue(dto.getSupplierId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Supplier with supplierId: " + dto.getSupplierId() + " not found or inactive"
                ));

        Warehouse warehouse = warehouseRepository.findByWarehouseIdAndActiveTrue(dto.getWarehouseId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Warehouse with warehouseId: " + dto.getWarehouseId() + " not found or inactive"
                ));

        PurchaseOrder order = new PurchaseOrder();
        order.setSupplier(supplier);
        order.setWarehouse(warehouse);
        order.setExpectedDeliveryDate(dto.getExpectedDeliveryDate());
        order.setStatus(PurchaseOrderStatus.PENDING);

        List<PurchaseOrderItem> items = new ArrayList<>();
        for(PurchaseOrderItemDto itemDto : dto.getItems()){
            ProductVariant variant = variantRepository.findByVariantIdAndActiveTrue(itemDto.getProductVariantId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Product Variant with variantId: " + itemDto.getProductVariantId() + " not found or inactive"
                    ));

            PurchaseOrderItem item = new PurchaseOrderItem();
            item.setPurchaseOrder(order);
            item.setProductVariant(variant);
            item.setQuantity(itemDto.getQuantity());
            item.setUnitCost(itemDto.getUnitCost());
            items.add(item);
        }
        order.setItems(items);

        PurchaseOrder saved = purchaseOrderRepository.save(order);
        return mapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public PurchaseOrderResponse getPurchaseOrderById(Long purchaseOrderId) {
        PurchaseOrder order = purchaseOrderRepository.findById(purchaseOrderId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Purchase Order with purchaseOrderId: " + purchaseOrderId + " not found"
                ));

        return mapper.toResponse(order);
    }

    @Override
    @Transactional(readOnly = true)
    public PurchaseOrderPageResponse getAllPurchaseOrders(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {

        Sort sort = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<PurchaseOrder> orderPage = purchaseOrderRepository.findAll(pageable);

        List<PurchaseOrderResponse> responses = orderPage.getContent()
                .stream()
                .map(mapper::toResponse)
                .toList();

        return PurchaseOrderPageResponse.builder()
                .content(responses)
                .pageNumber(orderPage.getNumber())
                .pageSize(orderPage.getSize())
                .totalElements(orderPage.getTotalElements())
                .totalPages(orderPage.getTotalPages())
                .lastPage(orderPage.isLast())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public PurchaseOrderPageResponse getPurchaseOrdersBySupplier(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder, Long supplierId) {

        Supplier supplier = supplierRepository.findBySupplierIdAndActiveTrue(supplierId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Supplier with supplierId: " + supplierId + " not found or inactive"
                ));

        Sort sort = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<PurchaseOrder> orderPage = purchaseOrderRepository.findBySupplier(supplier, pageable);

        List<PurchaseOrderResponse> responses = orderPage.getContent()
                .stream()
                .map(mapper::toResponse)
                .toList();

        return PurchaseOrderPageResponse.builder()
                .content(responses)
                .pageNumber(orderPage.getNumber())
                .pageSize(orderPage.getSize())
                .totalElements(orderPage.getTotalElements())
                .totalPages(orderPage.getTotalPages())
                .lastPage(orderPage.isLast())
                .build();
    }

    @Override
    @Transactional
    public PurchaseOrderResponse updateStatus(Long purchaseOrderId, PurchaseOrderStatusUpdateDto statusUpdateDto) {
        PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(purchaseOrderId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Purchase Order with purchaseOrderId: " + purchaseOrderId + " not found"
                ));

        if (purchaseOrder.getStatus() == PurchaseOrderStatus.RECEIVED || purchaseOrder.getStatus() == PurchaseOrderStatus.CANCELLED){
            throw new InvalidStatusTransitionException(
                    "Cannot change the status of a purchase order that is already " + purchaseOrder.getStatus()
            );
        }

        if (statusUpdateDto.getStatus() == PurchaseOrderStatus.RECEIVED){
            for (PurchaseOrderItem item : purchaseOrder.getItems()){
                ProductStock stock = stockRepository.findByProductVariantAndWarehouse(item.getProductVariant(), purchaseOrder.getWarehouse())
                        .orElse(null);

                if (stock == null) {
                    stock = new ProductStock();
                    stock.setProductVariant(item.getProductVariant());
                    stock.setWarehouse(purchaseOrder.getWarehouse());
                    stock.setQuantity(0);
                }

                stock.setQuantity(stock.getQuantity() + item.getQuantity());
                stockRepository.save(stock);
            }
        }

        purchaseOrder.setStatus(statusUpdateDto.getStatus());
        PurchaseOrder updated =
                purchaseOrderRepository.save(purchaseOrder);
        return mapper.toResponse(updated);
    }
}
