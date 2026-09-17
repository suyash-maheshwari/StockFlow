package com.Suyash.StockFlow.controller;

import com.Suyash.StockFlow.payload.request.AddCartItemDto;
import com.Suyash.StockFlow.payload.request.UpdateCartItemDto;
import com.Suyash.StockFlow.payload.response.CartResponse;
import com.Suyash.StockFlow.service.CartService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/carts")
@Validated
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<CartResponse> getOrCreateCart(@PathVariable @Min(1) Long userId){
        CartResponse response = cartService.getOrCreateCart(userId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/{cartId}/items")
    public ResponseEntity<CartResponse> addItem(@PathVariable @Min(1) Long cartId, @Valid @RequestBody AddCartItemDto addCartItemDto){
        CartResponse response = cartService.addItem(cartId, addCartItemDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{cartId}/items/{cartItemId}")
    public ResponseEntity<CartResponse> updateItemQuantity(@PathVariable @Min(1) Long cartId, @PathVariable @Min(1) Long cartItemId,
                                                           @Valid @RequestBody UpdateCartItemDto itemDto){
        CartResponse response = cartService.updateItemQuantity(cartId, cartItemId, itemDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{cartId}/items/{cartItemId}")
    public ResponseEntity<CartResponse> removeItem(@PathVariable @Min(1) Long cartId, @PathVariable @Min(1) Long cartItemId){
        CartResponse response = cartService.removeItem(cartId, cartItemId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{cartId}")
    public ResponseEntity<String> clearCart(@PathVariable @Min(1) Long cartId){
        String message = cartService.clearCart(cartId);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}
