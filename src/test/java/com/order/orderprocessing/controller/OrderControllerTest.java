package com.order.orderprocessing.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.order.orderprocessing.dto.CreateOrderRequest;
import com.order.orderprocessing.dto.OrderResponse;
import com.order.orderprocessing.entity.OrderStatus;
import com.order.orderprocessing.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
@ActiveProfiles("test")
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private OrderService orderService;

    @Test
    void createOrder_returnsCreated() throws Exception {
        UUID orderId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UUID itemId = UUID.randomUUID();

        CreateOrderRequest request = new CreateOrderRequest();
        request.setUserId(userId);
        request.setItemIds(List.of(itemId));
        request.setTotalAmount(new BigDecimal("49.99"));

        OrderResponse response = OrderResponse.builder()
                .orderId(orderId)
                .userId(userId)
                .itemIds(List.of(itemId))
                .totalAmount(new BigDecimal("49.99"))
                .status(OrderStatus.PENDING)
                .createdAt(OffsetDateTime.now())
                .build();

        when(orderService.createOrder(any())).thenReturn(response);

        mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.orderId").value(orderId.toString()))
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andExpect(jsonPath("$.totalAmount").value(49.99));
    }

    @Test
    void createOrder_missingUserId_returns400() throws Exception {
        CreateOrderRequest request = new CreateOrderRequest();
        request.setItemIds(List.of(UUID.randomUUID()));
        request.setTotalAmount(new BigDecimal("10.00"));

        mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createOrder_emptyItemIds_returns400() throws Exception {
        CreateOrderRequest request = new CreateOrderRequest();
        request.setUserId(UUID.randomUUID());
        request.setItemIds(List.of());
        request.setTotalAmount(new BigDecimal("10.00"));

        mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createOrder_negativeTotalAmount_returns400() throws Exception {
        CreateOrderRequest request = new CreateOrderRequest();
        request.setUserId(UUID.randomUUID());
        request.setItemIds(List.of(UUID.randomUUID()));
        request.setTotalAmount(new BigDecimal("-5.00"));

        mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
