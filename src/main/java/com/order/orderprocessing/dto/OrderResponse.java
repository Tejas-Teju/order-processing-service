package com.order.orderprocessing.dto;

import com.order.orderprocessing.entity.OrderStatus;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
public class OrderResponse {

    private UUID orderId;
    private UUID userId;
    private List<UUID> itemIds;
    private BigDecimal totalAmount;
    private OrderStatus status;
    private OffsetDateTime createdAt;
}