package com.Suyash.StockFlow.service.serviceImpl;

import com.Suyash.StockFlow.exceptions.ResourceNotFoundException;
import com.Suyash.StockFlow.model.Cart;
import com.Suyash.StockFlow.model.CartItem;
import com.Suyash.StockFlow.model.ProductVariant;
import com.Suyash.StockFlow.model.User;
import com.Suyash.StockFlow.payload.mapper.CartMapper;
import com.Suyash.StockFlow.payload.request.AddCartItemDto;
import com.Suyash.StockFlow.payload.request.UpdateCartItemDto;
import com.Suyash.StockFlow.payload.response.CartResponse;
import com.Suyash.StockFlow.repository.CartItemRepository;
import com.Suyash.StockFlow.repository.CartRepository;
import com.Suyash.StockFlow.repository.ProductVariantRepository;
import com.Suyash.StockFlow.repository.UserRepository;
import com.Suyash.StockFlow.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductVariantRepository variantRepository;

    @Autowired
    private CartMapper mapper;

    @Override
    public CartResponse getOrCreateCart(Long userId) {

        User user = userRepository.findByIdAndEnabledTrue(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User with userId: " + userId + " not found or inactive"
                ));

        Cart cart = cartRepository.findByUser(user)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                });

        return mapper.toResponse(cart);
    }

    @Override
    public CartResponse addItem(Long cartId, AddCartItemDto addCartItemDto) {

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Cart with cartId: " + cartId + " not found"
                ));

        ProductVariant variant = variantRepository.findByVariantIdAndActiveTrue(addCartItemDto.getProductVariantId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product Variant not found or inactive"
                ));

        CartItem existingItem = cartItemRepository.findByCartAndProductVariant(cart, variant).orElse(null);

        if(existingItem != null){
            existingItem.setQuantity(existingItem.getQuantity() + addCartItemDto.getQuantity());
            cartItemRepository.save(existingItem);
        } else{
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setProductVariant(variant);
            newItem.setQuantity(addCartItemDto.getQuantity());
            cartItemRepository.save(newItem);
        }

        Cart refreshed = cartRepository.findById(cartId).orElseThrow();
        return mapper.toResponse(refreshed);
    }

    @Override
    public CartResponse updateItemQuantity(Long cartId, Long cartItemId, UpdateCartItemDto itemDto) {

        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Cart item with cartItemId: " + cartItemId + " not found"
                ));

        if (!item.getCart().getCartId().equals(cartId)){
            throw new ResourceNotFoundException(
                    "Cart item with cartItemId: " + cartItemId + " does not belong to cart with cartId: " + cartId
            );
        }

        item.setQuantity(itemDto.getQuantity());
        cartItemRepository.save(item);

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Cart with cartId: " + cartId + " not found"
                ));
        return mapper.toResponse(cart);
    }

    @Override
    public CartResponse removeItem(Long cartId, Long cartItemId) {
        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Cart item with cartItemId: " + cartItemId + " not found"
                ));

        if(!item.getCart().getCartId().equals(cartId)){
            throw new ResourceNotFoundException(
                    "Cart item with cartItemId: " + cartItemId + " does not belong to cart with cartId: " + cartId
            );
        }

        cartItemRepository.deleteById(cartItemId);

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Cart with cartId: " + cartId + " not found"
                ));

        return mapper.toResponse(cart);
    }

    @Override
    @Transactional
    public String clearCart(Long cartId) {

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Cart with cartId: " + cartId + " not found"
                ));

        cart.getItems().clear();
        cartRepository.save(cart);
        return "Cart with cartId: " + cartId + " cleared successfully";
    }
}
