package com.Suyash.StockFlow.payload.mapper;

import com.Suyash.StockFlow.model.Address;
import com.Suyash.StockFlow.payload.request.AddressDto;
import com.Suyash.StockFlow.payload.response.AddressResponse;
import org.springframework.stereotype.Component;

@Component
public class AddressMapper {

    public AddressResponse toResponse(Address address){
        return AddressResponse.builder()
                .addressId(address.getAddressId())
                .userId(address.getUser().getId())
                .addressLine(address.getAddressLine())
                .city(address.getCity())
                .state(address.getState())
                .postalCode(address.getPostalCode())
                .country(address.getCountry())
                .isDefault(address.isDefault())
                .build();
    }

    public Address toEntity(AddressDto dto){
        Address address = new Address();
        address.setAddressLine(dto.getAddressLine());
        address.setCity(dto.getCity());
        address.setState(dto.getState());
        address.setPostalCode(dto.getPostalCode());
        address.setCountry(dto.getCountry());
        return address;
    }
}
