package com.Suyash.StockFlow.controller;

import com.Suyash.StockFlow.config.AppConstants;
import com.Suyash.StockFlow.payload.response.StockTransactionResponse;
import com.Suyash.StockFlow.payload.response.pageResponse.StockTransactionPageResponse;
import com.Suyash.StockFlow.service.StockTransactionService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/stock-transactions")
@Validated
public class StockTransactionController {

    @Autowired
    private StockTransactionService service;

    @GetMapping("/{transactionId}")
    public ResponseEntity<StockTransactionResponse> getTransactionById(@PathVariable Long transactionId){
        StockTransactionResponse response = service.getTransactionById(transactionId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<StockTransactionPageResponse> getAllTransactions(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER) @Min(0) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE) @Min(1) @Max(100) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_TRANSACTION_BY) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR) String sortOrder
    ){
        StockTransactionPageResponse response = service.getAllTransactions(pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/stock/{stockId}")
    public ResponseEntity<StockTransactionPageResponse> getTransactionsByStock(
            @PathVariable @Min(1) Long stockId,
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER) @Min(0) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE) @Min(1) @Max(100) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_TRANSACTION_BY) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR) String sortOrder
    ){
        StockTransactionPageResponse response = service.getTransactionsByStock(pageNumber, pageSize, sortBy, sortOrder, stockId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}