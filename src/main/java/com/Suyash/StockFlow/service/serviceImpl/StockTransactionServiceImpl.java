package com.Suyash.StockFlow.service.serviceImpl;

import com.Suyash.StockFlow.exceptions.ResourceNotFoundException;
import com.Suyash.StockFlow.model.ProductStock;
import com.Suyash.StockFlow.model.StockTransaction;
import com.Suyash.StockFlow.payload.mapper.StockTransactionMapper;
import com.Suyash.StockFlow.payload.response.StockTransactionResponse;
import com.Suyash.StockFlow.payload.response.pageResponse.StockTransactionPageResponse;
import com.Suyash.StockFlow.repository.ProductStockRepository;
import com.Suyash.StockFlow.repository.StockTransactionRepository;
import com.Suyash.StockFlow.service.StockTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StockTransactionServiceImpl implements StockTransactionService {

    @Autowired
    private StockTransactionRepository transactionRepository;

    @Autowired
    private ProductStockRepository stockRepository;

    @Autowired
    private StockTransactionMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public StockTransactionResponse getTransactionById(Long transactionId) {

        StockTransaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Stock Transaction with transactionId: " + transactionId + " not found"
                ));

        return mapper.toResponse(transaction);
    }

    @Override
    @Transactional(readOnly = true)
    public StockTransactionPageResponse getAllTransactions(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {

        Sort sort = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<StockTransaction> transactionPage = transactionRepository.findAll(pageable);

        List<StockTransactionResponse> responses = transactionPage.getContent()
                .stream()
                .map(mapper::toResponse)
                .toList();

        return StockTransactionPageResponse.builder()
                .content(responses)
                .pageNumber(transactionPage.getNumber())
                .pageSize(transactionPage.getSize())
                .totalElements(transactionPage.getTotalElements())
                .totalPages(transactionPage.getTotalPages())
                .lastPage(transactionPage.isLast())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public StockTransactionPageResponse getTransactionsByStock(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder, Long stockId) {

        ProductStock stock = stockRepository.findById(stockId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Stock with stockId: " + stockId + " not found"
                ));

        Sort sort = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<StockTransaction> transactionPage = transactionRepository.findByProductStock(stock, pageable);

        List<StockTransactionResponse> responses = transactionPage.getContent()
                .stream()
                .map(mapper::toResponse)
                .toList();

        return StockTransactionPageResponse.builder()
                .content(responses)
                .pageNumber(transactionPage.getNumber())
                .pageSize(transactionPage.getSize())
                .totalElements(transactionPage.getTotalElements())
                .totalPages(transactionPage.getTotalPages())
                .lastPage(transactionPage.isLast())
                .build();
    }
}
