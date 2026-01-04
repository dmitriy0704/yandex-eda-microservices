package dev.folomkin.api.http.order;

import java.util.Set;

public record CreateOrderRequestDto(

        Long customerId,
        String address,
        Set<dev.folomkin.api.http.order.OrderItemRequestDto> items
) {
}
