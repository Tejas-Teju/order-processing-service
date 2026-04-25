package com.order.orderprocessing.service;

import com.order.orderprocessing.dto.CreateOrderRequest;
import com.order.orderprocessing.dto.OrderResponse;
import com.order.orderprocessing.entity.Order;
import com.order.orderprocessing.entity.OrderItem;
import com.order.orderprocessing.entity.OrderStatus;
import com.order.orderprocessing.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    @Transactional
    public OrderResponse createOrder(CreateOrderRequest request) {
        Order order = new Order();
        order.setUserId(request.getUserId());
        order.setTotalAmount(request.getTotalAmount());
        order.setStatus(OrderStatus.PENDING);
        order.setCreatedAt(OffsetDateTime.now());

        List<OrderItem> items = request.getItemIds().stream()
                .map(itemId -> {
                    OrderItem item = new OrderItem();
                    item.setItemId(itemId);
                    item.setOrder(order);
                    return item;
                })
                .toList();
        order.setItems(items);

        Order saved = orderRepository.save(order);
        return toResponse(saved);
    }

    private OrderResponse toResponse(Order order) {
        return OrderResponse.builder()
                .orderId(order.getId())
                .userId(order.getUserId())
                .itemIds(order.getItems().stream().map(OrderItem::getItemId).toList())
                .totalAmount(order.getTotalAmount())
                .status(order.getStatus())
                .createdAt(order.getCreatedAt())
                .build();
    }
}
