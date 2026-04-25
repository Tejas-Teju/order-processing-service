package com.order.orderprocessing.repository;

import com.order.orderprocessing.entity.Order;
import com.order.orderprocessing.entity.OrderItem;
import com.order.orderprocessing.entity.OrderStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Test
    void savesOrderWithItems() {
        Order order = new Order();
        order.setUserId(UUID.randomUUID());
        order.setTotalAmount(new BigDecimal("49.99"));
        order.setStatus(OrderStatus.PENDING);
        order.setCreatedAt(OffsetDateTime.now());

        OrderItem item = new OrderItem();
        item.setItemId(UUID.randomUUID());
        item.setOrder(order);
        order.getItems().add(item);

        Order saved = orderRepository.save(order);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getStatus()).isEqualTo(OrderStatus.PENDING);
        assertThat(saved.getItems()).hasSize(1);
        assertThat(saved.getItems().get(0).getItemId()).isNotNull();
    }

    @Test
    void findsOrderById() {
        Order order = new Order();
        order.setUserId(UUID.randomUUID());
        order.setTotalAmount(new BigDecimal("19.99"));
        order.setStatus(OrderStatus.PENDING);
        order.setCreatedAt(OffsetDateTime.now());

        Order saved = orderRepository.save(order);

        Optional<Order> found = orderRepository.findById(saved.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getUserId()).isEqualTo(saved.getUserId());
        assertThat(found.get().getTotalAmount()).isEqualByComparingTo(new BigDecimal("19.99"));
    }
}
