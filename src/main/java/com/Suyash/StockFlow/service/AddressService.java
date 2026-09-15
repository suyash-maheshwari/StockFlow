package com.Suyash.StockFlow.service;

import com.Suyash.StockFlow.payload.request.AddressDto;
import com.Suyash.StockFlow.payload.response.AddressResponse;

import java.util.List;

public interface AddressService {
    AddressResponse createAddress(Long userId, AddressDto addressDto);

    AddressResponse getAddressById(Long addressId);

    List<AddressResponse> getAddressesForUser(Long userId);

    AddressResponse updateAddress(Long addressId, AddressDto addressDto);

    AddressResponse setDefault(Long addressId);

    String deleteAddress(Long addressId);
}
