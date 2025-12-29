package dev.folomkin.orderservice.api;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO for {@link dev.folomkin.orderservice.domain.OrderItemEntity}
 */
public record OrderItemDto(Long id,
                           Long itemId,
                           Integer quantity,
                           BigDecimal priceAtPurchase) implements Serializable {
}