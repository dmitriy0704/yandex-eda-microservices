package dev.folomkin.orderservice.api;

import dev.folomkin.orderservice.domain.OrderStatus;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Set;

/**
 * DTO for {@link dev.folomkin.orderservice.domain.OrderEntity}
 */
public record OrderDto(Long id,
                       Long customerId,
                       String address,
                       BigDecimal totalAmount,
                       String courierName,
                       Integer etaMinutes,
                       OrderStatus orderStatus,
                       Set<OrderItemDto> orderItemEntities) implements Serializable {
}