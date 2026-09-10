package com.Suyash.StockFlow.payload.mapper;

import com.Suyash.StockFlow.model.Warehouse;
import com.Suyash.StockFlow.payload.request.WarehouseDto;
import com.Suyash.StockFlow.payload.response.WarehouseResponse;
import org.springframework.stereotype.Component;

@Component
public class WarehouseMapper {
    public WarehouseResponse toResponse(Warehouse warehouse){
        return WarehouseResponse.builder()
                .warehouseId(warehouse.getWarehouseId())
                .warehouseName(warehouse.getWarehouseName())
                .addressLine(warehouse.getAddressLine())
                .city(warehouse.getCity())
                .state(warehouse.getState())
                .postalCode(warehouse.getPostalCode())
                .country(warehouse.getCountry())
                .contactNumber(warehouse.getContactNumber())
                .active(warehouse.isActive())
                .build();
    }

    public Warehouse toEntity(WarehouseDto dto){
        Warehouse warehouse = new Warehouse();
        warehouse.setWarehouseName(dto.getWarehouseName());
        warehouse.setAddressLine(dto.getAddressLine());
        warehouse.setCity(dto.getCity());
        warehouse.setState(dto.getState());
        warehouse.setPostalCode(dto.getPostalCode());
        warehouse.setCountry(dto.getCountry());
        warehouse.setContactNumber(dto.getContactNumber());

        return warehouse;
    }
}
