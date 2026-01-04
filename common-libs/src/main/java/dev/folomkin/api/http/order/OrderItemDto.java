package dev.folomkin.api.http.order;

import java.io.Serializable;
import java.math.BigDecimal;

public record OrderItemDto(Long id,
                           Long itemId,
                           Integer quantity,
                           BigDecimal priceAtPurchase) implements Serializable {
}