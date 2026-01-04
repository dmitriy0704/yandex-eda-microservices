package dev.folomkin.orderservice.external;


import dev.folomkin.api.http.payment.CreatePaymentRequestDto;
import dev.folomkin.api.http.payment.CreatePaymentResponseDto;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange(
        accept = "application/json",
        contentType = "application/json",
        url = "/api/payments"
)
public interface PaymentHttpClient {
    
    @PostExchange
    CreatePaymentResponseDto createPayment(CreatePaymentRequestDto requestDto);
}
