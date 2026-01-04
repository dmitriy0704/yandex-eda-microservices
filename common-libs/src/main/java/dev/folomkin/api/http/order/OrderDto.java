package dev.folomkin.api.http.order;


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Set;

/**
 */
public record OrderDto(Long id,
                       Long customerId,
                       String address,
                       BigDecimal totalAmount,
                       String courierName,
                       Integer etaMinutes,
                       OrderStatus orderStatus,
                       Set<OrderItemDto> items
) implements Serializable {
}