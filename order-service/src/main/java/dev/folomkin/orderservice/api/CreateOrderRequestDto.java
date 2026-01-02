package dev.folomkin.orderservice.api;

import dev.folomkin.orderservice.domain.OrderStatus;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

public record CreateOrderRequestDto(

        Long customerId,
        String address,
        Set<OrderItemRequestDto> items
) {
}
