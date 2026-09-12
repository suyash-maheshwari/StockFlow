package com.Suyash.StockFlow.service.serviceImpl;

import com.Suyash.StockFlow.exceptions.DuplicateResourceFoundException;
import com.Suyash.StockFlow.exceptions.ResourceNotFoundException;
import com.Suyash.StockFlow.model.Supplier;
import com.Suyash.StockFlow.payload.mapper.SupplierMapper;
import com.Suyash.StockFlow.payload.request.SupplierDto;
import com.Suyash.StockFlow.payload.response.SupplierResponse;
import com.Suyash.StockFlow.payload.response.pageResponse.SupplierPageResponse;
import com.Suyash.StockFlow.repository.SupplierRepository;
import com.Suyash.StockFlow.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SupplierServiceImpl implements SupplierService {

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private SupplierMapper mapper;

    @Override
    public SupplierResponse createSupplier(SupplierDto supplierDto) {

        String trimmedName = supplierDto.getSupplierName().trim();

        supplierRepository.findBySupplierNameIgnoreCase(trimmedName)
                .ifPresent(existing -> {
                    throw new DuplicateResourceFoundException("Supplier with supplier " + trimmedName + " already exists");
                });

        Supplier supplier = new Supplier();
        supplier.setSupplierName(trimmedName);
        supplier.setActive(true);

        Supplier saved = supplierRepository.save(supplier);
        return mapper.toResponse(supplier);
    }

    @Override
    @Transactional(readOnly = true)
    public SupplierResponse getSupplierById(Long supplierId) {

        Supplier supplier = supplierRepository.findBySupplierIdAndActiveTrue(supplierId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Supplier with supplierId: " + supplierId + " not found or inactive"
                ));

        return mapper.toResponse(supplier);
    }

    @Override
    @Transactional(readOnly = true)
    public SupplierPageResponse getAllSuppliers(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {

        Sort sort = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Supplier> supplierPage = supplierRepository.findByActiveTrue(pageable);

        List<SupplierResponse> responses = supplierPage.getContent()
                .stream()
                .map(mapper::toResponse)
                .toList();

        return SupplierPageResponse.builder()
                .content(responses)
                .pageNumber(supplierPage.getNumber())
                .pageSize(supplierPage.getSize())
                .totalElements(supplierPage.getTotalElements())
                .totalPages(supplierPage.getTotalPages())
                .lastPage(supplierPage.isLast())
                .build();
    }

    @Override
    public SupplierResponse updateSupplier(Long supplierId, SupplierDto supplierDto) {

        Supplier existing = supplierRepository.findBySupplierIdAndActiveTrue(supplierId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Supplier with supplierId: " + supplierId + " not found or Inactive"
                ));

        String trimmedName = supplierDto.getSupplierName().trim();
        supplierRepository.findBySupplierNameIgnoreCase(trimmedName)
                .ifPresent(matched -> {
                    if (matched.getSupplierId().equals(supplierId)){
                        throw new DuplicateResourceFoundException(
                                "Supplier with name " + trimmedName + " already exists"
                        );
                    }
                });

        existing.setSupplierName(trimmedName);
        existing.setContactPerson(supplierDto.getContactPerson());
        existing.setContactNumber(supplierDto.getContactNumber());
        existing.setEmail(supplierDto.getEmail());
        existing.setAddressLine(supplierDto.getAddressLine());
        existing.setCity(supplierDto.getCity());
        existing.setState(supplierDto.getState());
        existing.setPostalCode(supplierDto.getPostalCode());
        existing.setCountry(supplierDto.getCountry());

        Supplier supplier = supplierRepository.save(existing);
        return mapper.toResponse(supplier);
    }

    @Override
    public String deactivateSupplier(Long supplierId) {

        Supplier supplier = supplierRepository.findBySupplierIdAndActiveTrue(supplierId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Supplier with supplierId: " + supplierId + " not found or inactive"
                ));

        supplier.setActive(false);
        supplierRepository.save(supplier);
        return "Supplier with supplierId: " + supplierId + " deactivated successfully";
    }
}
