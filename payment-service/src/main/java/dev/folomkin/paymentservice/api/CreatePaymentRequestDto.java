package dev.folomkin.paymentservice.api;

import dev.folomkin.paymentservice.domain.PaymentMethod;

import java.math.BigDecimal;

public record CreatePaymentRequestDto(
        Long orderId,
        PaymentMethod paymentMethod,
        BigDecimal amount

) {
}
