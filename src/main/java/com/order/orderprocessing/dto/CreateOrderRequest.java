package com.order.orderprocessing.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class CreateOrderRequest {

    @NotNull(message = "userId is required")
    private UUID userId;

    @NotEmpty(message = "itemIds must not be empty")
    private List<@NotNull UUID> itemIds;

    @NotNull(message = "totalAmount is required")
    @Positive(message = "totalAmount must be positive")
    private BigDecimal totalAmount;
}