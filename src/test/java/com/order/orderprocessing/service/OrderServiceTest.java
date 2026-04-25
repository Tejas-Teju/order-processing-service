package com.order.orderprocessing.service;

import com.order.orderprocessing.dto.CreateOrderRequest;
import com.order.orderprocessing.dto.OrderResponse;
import com.order.orderprocessing.entity.Order;
import com.order.orderprocessing.entity.OrderStatus;
import com.order.orderprocessing.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    @Test
    void createOrder_setsPendingStatusAndMapsFields() {
        UUID userId = UUID.randomUUID();
        UUID itemId = UUID.randomUUID();

        CreateOrderRequest request = new CreateOrderRequest();
        request.setUserId(userId);
        request.setItemIds(List.of(itemId));
        request.setTotalAmount(new BigDecimal("29.99"));

        when(orderRepository.save(any(Order.class))).thenAnswer(inv -> inv.getArgument(0));

        OrderResponse response = orderService.createOrder(request);

        assertThat(response.getStatus()).isEqualTo(OrderStatus.PENDING);
        assertThat(response.getUserId()).isEqualTo(userId);
        assertThat(response.getItemIds()).containsExactly(itemId);
        assertThat(response.getTotalAmount()).isEqualByComparingTo(new BigDecimal("29.99"));
        assertThat(response.getCreatedAt()).isNotNull();
    }

    @Test
    void createOrder_setsCreatedAtToNow() {
        CreateOrderRequest request = new CreateOrderRequest();
        request.setUserId(UUID.randomUUID());
        request.setItemIds(List.of(UUID.randomUUID()));
        request.setTotalAmount(new BigDecimal("10.00"));

        when(orderRepository.save(any(Order.class))).thenAnswer(inv -> inv.getArgument(0));

        OrderResponse response = orderService.createOrder(request);

        assertThat(response.getCreatedAt()).isNotNull();
        assertThat(response.getCreatedAt()).isBeforeOrEqualTo(
                java.time.OffsetDateTime.now().plusSeconds(1));
    }
}
