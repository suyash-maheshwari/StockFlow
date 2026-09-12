package com.Suyash.StockFlow.exceptions;

public class InvalidTransferException extends RuntimeException{
    public InvalidTransferException(String message){
        super(message);
    }
}
