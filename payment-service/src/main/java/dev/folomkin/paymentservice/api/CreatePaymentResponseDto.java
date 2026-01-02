package dev.folomkin.paymentservice.api;

import dev.folomkin.paymentservice.domain.PaymentMethod;
import dev.folomkin.paymentservice.domain.PaymentStatus;

import java.math.BigDecimal;

public record CreatePaymentResponseDto(
        Long paymentId,
        PaymentStatus paymentStatus,
        Long orderId,
        PaymentMethod paymentMethod,
        BigDecimal amount

) {
}
