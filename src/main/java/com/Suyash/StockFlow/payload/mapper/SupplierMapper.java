package com.Suyash.StockFlow.payload.mapper;

import com.Suyash.StockFlow.model.Supplier;
import com.Suyash.StockFlow.payload.request.SupplierDto;
import com.Suyash.StockFlow.payload.response.SupplierResponse;
import org.springframework.stereotype.Component;

@Component
public class SupplierMapper {

    public SupplierResponse toResponse(Supplier supplier){
        return SupplierResponse.builder()
                .supplierId(supplier.getSupplierId())
                .supplierName(supplier.getSupplierName())
                .contactPerson(supplier.getContactPerson())
                .contactNumber(supplier.getContactNumber())
                .email(supplier.getEmail())
                .addressLine(supplier.getAddressLine())
                .city(supplier.getCity())
                .state(supplier.getState())
                .postalCode(supplier.getPostalCode())
                .country(supplier.getCountry())
                .active(supplier.isActive())
                .build();
    }

    public Supplier toEntity(SupplierDto dto){
        Supplier supplier = new Supplier();
        supplier.setSupplierName(dto.getSupplierName());
        supplier.setContactPerson(dto.getContactPerson());
        supplier.setContactNumber(dto.getContactNumber());
        supplier.setEmail(dto.getEmail());
        supplier.setAddressLine(dto.getAddressLine());
        supplier.setCity(dto.getCity());
        supplier.setState(dto.getState());
        supplier.setPostalCode(dto.getPostalCode());
        supplier.setCountry(dto.getCountry());

        return supplier;
    }
}
