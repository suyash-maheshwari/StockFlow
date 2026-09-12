package com.Suyash.StockFlow.controller;

import com.Suyash.StockFlow.config.AppConstants;
import com.Suyash.StockFlow.payload.request.StockTransferDto;
import com.Suyash.StockFlow.payload.response.StockTransferResponse;
import com.Suyash.StockFlow.payload.response.pageResponse.StockTransferPageResponse;
import com.Suyash.StockFlow.service.StockTransferService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/stock-transfers")
@Validated
public class StockTransferController {

    @Autowired
    private StockTransferService transferService;

    @PostMapping
    public ResponseEntity<StockTransferResponse> executeTransfer(@RequestBody StockTransferDto transferDto){
        StockTransferResponse response = transferService.executeTransfer(transferDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{transferId}")
    public ResponseEntity<StockTransferResponse> getTransferById(@PathVariable @Min(1) Long transferId){
        StockTransferResponse response = transferService.getTransferById(transferId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<StockTransferPageResponse> getAllTransfers(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER) @Min(0) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE) @Min(1) @Max(100) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_TRANSFER_BY) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR) String sortOrder
    ){
        StockTransferPageResponse response = transferService.getAllTransfers(pageNumber, pageSize, sortBy, sortOrder);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
