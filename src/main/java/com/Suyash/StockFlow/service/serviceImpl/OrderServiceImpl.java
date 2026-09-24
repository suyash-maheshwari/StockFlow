package com.Suyash.StockFlow.service.serviceImpl;

import com.Suyash.StockFlow.enums.OrderStatus;
import com.Suyash.StockFlow.exceptions.InsufficientStockException;
import com.Suyash.StockFlow.exceptions.InvalidOrderException;
import com.Suyash.StockFlow.exceptions.ResourceNotFoundException;
import com.Suyash.StockFlow.model.*;
import com.Suyash.StockFlow.payload.mapper.OrderMapper;
import com.Suyash.StockFlow.payload.request.OrderStatusUpdateDto;
import com.Suyash.StockFlow.payload.request.PlacedOrderDto;
import com.Suyash.StockFlow.payload.response.OrderResponse;
import com.Suyash.StockFlow.payload.response.pageResponse.OrderPageResponse;
import com.Suyash.StockFlow.repository.*;
import com.Suyash.StockFlow.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository itemRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductStockRepository stockRepository;

    @Autowired
    private OrderMapper mapper;

    @Override
    @Transactional
    public OrderResponse placeOrder(Long userId, PlacedOrderDto dto) {

        User user = userRepository.findByIdAndEnabledTrue(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User with userId: " + userId + " not found or inactive"
                ));

        Address address = addressRepository.findById(dto.getShippingAddressId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Shipping address not found."
                ));

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No cart found with this user"
                ));

        if(cart.getItems().isEmpty()){
            throw new InvalidOrderException(
                    "Cannot place an order with an empty cart"
            );
        }

        Order order = new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.PLACED);
        order.setShippingAddressLine(address.getAddressLine());
        order.setShippingCity(address.getCity());
        order.setShippingState(address.getState());
        order.setShippingPostalCode(address.getPostalCode());
        order.setShippingCountry(address.getCountry());

        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

            for (CartItem cartItem : cart.getItems()){
                ProductVariant variant = cartItem.getProductVariant();

                int totalAvailable = stockRepository.sumQuantityByProductVariant(variant);

                if(totalAvailable < cartItem.getQuantity()){
                    throw new InsufficientStockException(
                            "Insufficient stock for " + variant.getVariantName() +
                                    " — requested " + cartItem.getQuantity() + ", only " + totalAvailable + " available"
                    );
                }

                int remainingToDeduct = cartItem.getQuantity();
                List<ProductStock> stocksForVariant = stockRepository.findByProductVariant(variant);

                for (ProductStock stock: stocksForVariant){
                    if (remainingToDeduct <= 0) break;
                    int deductFromThis = Math.min(stock.getQuantity(), remainingToDeduct);
                    stock.setQuantity(stock.getQuantity() - deductFromThis);
                    stockRepository.save(stock);
                    remainingToDeduct -= deductFromThis;
                }

                BigDecimal priceAtPurchase = variant.getPrice();
                BigDecimal subTotal = priceAtPurchase.multiply(BigDecimal.valueOf(cartItem.getQuantity()));
                total = total.add(subTotal);

                OrderItem orderItem = new OrderItem();
                orderItem.setOrder(order);
                orderItem.setProductVariant(variant);
                orderItem.setQuantity(cartItem.getQuantity());
                orderItem.setPriceAtPurchase(priceAtPurchase);
                orderItem.setSubtotal(subTotal);
                orderItems.add(orderItem);
            }

            order.setItems(orderItems);
            order.setTotalAmount(total);

            Order savedOrder = orderRepository.save(order);
            cart.getItems().clear();
            cartRepository.save(cart);
            return mapper.toResponse(savedOrder);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse getOrderById(Long orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Order with orderId: " + orderId + " not found"
                ));
        return mapper.toResponse(order);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderPageResponse getAllOrders(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {

        Sort sort = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        Page<Order> orderPage = orderRepository.findAll(pageable);
        List<OrderResponse> responses = orderPage.getContent()
                .stream()
                .map(mapper::toResponse)
                .toList();
        return OrderPageResponse.builder()
                .content(responses)
                .pageNumber(orderPage.getNumber())
                .pageSize(orderPage.getSize())
                .totalElements(orderPage.getTotalElements())
                .totalPages(orderPage.getTotalPages())
                .lastPage(orderPage.isLast())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public OrderPageResponse getOrdersForUser(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder, Long userId) {

        User user = userRepository.findByIdAndEnabledTrue(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User with userId: " + userId + " not found or inactive"
                ));

        Sort sort = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        Page<Order> orderPage = orderRepository.findByUser(user, pageable);

        List<OrderResponse> responses = orderPage.getContent()
                .stream()
                .map(mapper::toResponse)
                .toList();

        return OrderPageResponse.builder()
                .content(responses)
                .pageNumber(orderPage.getNumber())
                .pageSize(orderPage.getSize())
                .totalElements(orderPage.getTotalElements())
                .totalPages(orderPage.getTotalPages())
                .lastPage(orderPage.isLast())
                .build();
    }

    @Override
    public OrderResponse updateStatus(Long orderId, OrderStatusUpdateDto dto) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Order with orderId: " + orderId + " not found"
                ));

        if (order.getStatus() == OrderStatus.DELIVERED || order.getStatus() == OrderStatus.CANCELLED){
            throw new InvalidOrderException(
                    "Cannot change the status of an order that is already " + order.getStatus()
            );
        }

        order.setStatus(dto.getStatus());
        Order updated = orderRepository.save(order);
        return mapper.toResponse(updated);
    }
}
