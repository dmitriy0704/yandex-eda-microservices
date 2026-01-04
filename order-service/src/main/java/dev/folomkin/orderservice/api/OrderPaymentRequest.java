package dev.folomkin.orderservice.api;

import dev.folomkin.api.http.payment.PaymentMethod;

public record OrderPaymentRequest(PaymentMethod paymentMethod) { }