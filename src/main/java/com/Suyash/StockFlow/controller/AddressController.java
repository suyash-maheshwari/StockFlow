package com.Suyash.StockFlow.controller;

import com.Suyash.StockFlow.payload.request.AddressDto;
import com.Suyash.StockFlow.payload.response.AddressResponse;
import com.Suyash.StockFlow.service.AddressService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/addresses")
@Validated
public class AddressController {

    @Autowired
    private AddressService addressService;

    @PostMapping("/user/{userId}")
    public ResponseEntity<AddressResponse> addAddress(@PathVariable @Min(1) Long userId, @Valid @RequestBody AddressDto addressDto){
        AddressResponse response = addressService.createAddress(userId, addressDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{addressId}")
    public ResponseEntity<AddressResponse> getAddressById(@PathVariable @Min(1) Long addressId){
        AddressResponse response = addressService.getAddressById(addressId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AddressResponse>> getAddressesForUser(@PathVariable @Min(1) Long userId){
        List<AddressResponse> responses = addressService.getAddressesForUser(userId);
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    @PutMapping("/{addressId}")
    public ResponseEntity<AddressResponse> updateAddress(@PathVariable @Min(1) Long addressId, @RequestBody AddressDto addressDto){
        AddressResponse response = addressService.updateAddress(addressId, addressDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("/{addressId}/default")
    public ResponseEntity<AddressResponse> setDefault(@PathVariable @Min(1) Long addressId){
        AddressResponse response = addressService.setDefault(addressId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{addressId}")
    public ResponseEntity<String> deleteAddress(@PathVariable @Min(1) Long addressId){
        String message = addressService.deleteAddress(addressId);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}
