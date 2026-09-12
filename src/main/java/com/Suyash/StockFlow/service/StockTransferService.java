package com.Suyash.StockFlow.service;

import com.Suyash.StockFlow.payload.request.StockTransferDto;
import com.Suyash.StockFlow.payload.response.StockTransferResponse;
import com.Suyash.StockFlow.payload.response.pageResponse.StockTransferPageResponse;

public interface StockTransferService {
    StockTransferResponse executeTransfer(StockTransferDto transferDto);

    StockTransferResponse getTransferById(Long transferId);

    StockTransferPageResponse getAllTransfers(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
}
