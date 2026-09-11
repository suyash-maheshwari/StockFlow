package com.Suyash.StockFlow.service;

import com.Suyash.StockFlow.payload.response.StockTransactionResponse;
import com.Suyash.StockFlow.payload.response.pageResponse.StockTransactionPageResponse;

public interface StockTransactionService {
    StockTransactionResponse getTransactionById(Long transactionId);

    StockTransactionPageResponse getAllTransactions(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    StockTransactionPageResponse getTransactionsByStock(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder, Long stockId);
}
