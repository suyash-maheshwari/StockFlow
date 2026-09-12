package com.Suyash.StockFlow.service;

import com.Suyash.StockFlow.payload.request.SupplierDto;
import com.Suyash.StockFlow.payload.response.SupplierResponse;
import com.Suyash.StockFlow.payload.response.pageResponse.SupplierPageResponse;

public interface SupplierService {
    SupplierResponse createSupplier(SupplierDto supplierDto);

    SupplierResponse getSupplierById(Long supplierId);

    SupplierPageResponse getAllSuppliers(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    SupplierResponse updateSupplier(Long supplierId, SupplierDto supplierDto);

    String deactivateSupplier(Long supplierId);
}
