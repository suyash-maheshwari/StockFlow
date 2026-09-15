package com.Suyash.StockFlow.service.serviceImpl;

import com.Suyash.StockFlow.exceptions.ResourceNotFoundException;
import com.Suyash.StockFlow.model.Address;
import com.Suyash.StockFlow.model.User;
import com.Suyash.StockFlow.payload.mapper.AddressMapper;
import com.Suyash.StockFlow.payload.request.AddressDto;
import com.Suyash.StockFlow.payload.response.AddressResponse;
import com.Suyash.StockFlow.repository.AddressRepository;
import com.Suyash.StockFlow.repository.UserRepository;
import com.Suyash.StockFlow.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private AddressMapper mapper;

    @Override
    public AddressResponse createAddress(Long userId, AddressDto addressDto) {
        User user = userRepository.findByIdAndEnabledTrue(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User with userId: " + userId + " not found"
                ));

        Address address = new Address();
        address.setUser(user);
        Address saved = addressRepository.save(address);
        return mapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public AddressResponse getAddressById(Long addressId) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Address with addressId: " + addressId + " not found"
                ));
        return mapper.toResponse(address);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AddressResponse> getAddressesForUser(Long userId) {
        User user = userRepository.findByIdAndEnabledTrue(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User with userId: " + userId + " not found"
                ));

        List<AddressResponse> addresses = addressRepository.findByUser(user)
                .stream()
                .map(mapper::toResponse)
                .toList();
        return addresses;
    }

    @Override
    public AddressResponse updateAddress(Long addressId, AddressDto addressDto) {

        Address existing = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Address with addressId: " + addressId + " not found"
                ));

        existing.setAddressLine(addressDto.getAddressLine());
        existing.setCity(addressDto.getCity());
        existing.setState(addressDto.getState());
        existing.setPostalCode(addressDto.getPostalCode());
        existing.setCountry(addressDto.getCountry());

        Address updated = addressRepository.save(existing);
        return mapper.toResponse(updated);
    }

    @Override
    @Transactional
    public AddressResponse setDefault(Long addressId) {

        Address newDefault = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Address with addressId: " + addressId + " not found"
                ));

        List<Address> allForUser = addressRepository.findByUser(newDefault.getUser());

        for (Address address: allForUser){
            if (address.isDefault() && !address.getAddressId().equals(addressId)){
                address.setDefault(false);
                addressRepository.save(address);
            }
        }

        newDefault.setDefault(true);
        Address saved = addressRepository.save(newDefault);
        return mapper.toResponse(saved);
    }

    @Override
    public String deleteAddress(Long addressId) {
        addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Address with addressId: " + addressId + " not found"
                ));
        addressRepository.deleteById(addressId);
        return "Address with addressId: " + addressId + " deleted successfully";
    }
}
