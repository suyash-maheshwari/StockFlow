package com.Suyash.StockFlow.service.serviceImpl;

import com.Suyash.StockFlow.exceptions.DuplicateResourceFoundException;
import com.Suyash.StockFlow.exceptions.ResourceNotFoundException;
import com.Suyash.StockFlow.model.Warehouse;
import com.Suyash.StockFlow.payload.mapper.WarehouseMapper;
import com.Suyash.StockFlow.payload.request.WarehouseDto;
import com.Suyash.StockFlow.payload.response.WarehouseResponse;
import com.Suyash.StockFlow.payload.response.pageResponse.WarehousePageResponse;
import com.Suyash.StockFlow.repository.WarehouseRepository;
import com.Suyash.StockFlow.service.WarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class WarehouseServiceImpl implements WarehouseService {

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Autowired
    private WarehouseMapper mapper;

    @Override
    public WarehouseResponse createWarehouse(WarehouseDto warehouseDto) {
        String trimmedName = warehouseDto.getWarehouseName().trim();
        warehouseRepository.findByWarehouseNameIgnoreCase(trimmedName)
                .ifPresent(existing -> {
                    throw new DuplicateResourceFoundException(
                            "Warehouse with name: " + trimmedName + " already exists"
                    );
                });
        Warehouse warehouse = mapper.toEntity(warehouseDto);
        warehouse.setActive(true);

        Warehouse savedWarehouse = warehouseRepository.save(warehouse);
        return mapper.toResponse(savedWarehouse);
    }

    @Override
    @Transactional(readOnly = true)
    public WarehouseResponse getWarehouseById(Long warehouseId) {
        Warehouse warehouse = warehouseRepository.findByWarehouseIdAndActiveTrue(warehouseId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Warehouse with warehouseId: " + warehouseId + " not found"
                ));

        return mapper.toResponse(warehouse);
    }

    @Override
    @Transactional(readOnly = true)
    public WarehousePageResponse getAllWarehouses(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {

        Sort sort = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Warehouse> pageDetails = warehouseRepository.findByActiveTrue(pageable);

        List<WarehouseResponse> responses = pageDetails.getContent()
                .stream()
                .map(mapper :: toResponse)
                .toList();

        return WarehousePageResponse.builder()
                .content(responses)
                .pageNumber(pageDetails.getNumber())
                .pageSize(pageDetails.getSize())
                .totalElements(pageDetails.getTotalElements())
                .totalPages(pageDetails.getTotalPages())
                .lastPage(pageDetails.isLast())
                .build();
    }

    @Override
    public WarehouseResponse updateWarehouse(Long warehouseId, WarehouseDto warehouseDto) {

        Warehouse existingWarehouse = warehouseRepository.findByWarehouseIdAndActiveTrue(warehouseId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Warehouse with warehouseId: " + warehouseId + " not found"
                ));

        warehouseRepository.findByWarehouseNameIgnoreCase(warehouseDto.getWarehouseName())
                .ifPresent(matched -> {
                    if(!matched.getWarehouseId().equals(warehouseId)){
                        throw new DuplicateResourceFoundException(
                                "Warehouse with name " + warehouseDto.getWarehouseName() + " already exists."
                        );
                    }
                });

        existingWarehouse.setWarehouseName(warehouseDto.getWarehouseName());
        existingWarehouse.setAddressLine(warehouseDto.getAddressLine());
        existingWarehouse.setCity(warehouseDto.getCity());
        existingWarehouse.setState(warehouseDto.getState());
        existingWarehouse.setPostalCode(warehouseDto.getPostalCode());
        existingWarehouse.setCountry(warehouseDto.getCountry());
        existingWarehouse.setContactNumber(warehouseDto.getContactNumber());

        Warehouse updatedWarehouse = warehouseRepository.save(existingWarehouse);
        return mapper.toResponse(updatedWarehouse);
    }

    @Override
    public String deactivateWarehouse(Long warehouseId) {
        Warehouse warehouse = warehouseRepository.findByWarehouseIdAndActiveTrue(warehouseId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Warehouse with warehouseId: " + warehouseId + " not found"
                ));

        warehouse.setActive(false);
        warehouseRepository.save(warehouse);
        return "Warehouse with warehouseId: " + warehouseId + " deactivated successfully";
    }
}
