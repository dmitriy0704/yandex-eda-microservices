package dev.folomkin.orderservice.domain;

import dev.folomkin.api.http.order.CreateOrderRequestDto;
import dev.folomkin.api.http.order.OrderStatus;
import dev.folomkin.api.http.payment.CreatePaymentRequestDto;
import dev.folomkin.api.http.payment.CreatePaymentResponseDto;
import dev.folomkin.api.http.payment.PaymentStatus;
import dev.folomkin.api.kafka.OrderPaidEvent;
import dev.folomkin.orderservice.api.OrderPaymentRequest;
import dev.folomkin.orderservice.domain.db.OrderEntity;
import dev.folomkin.orderservice.domain.db.OrderEntityMapper;
import dev.folomkin.orderservice.domain.db.OrderItemEntity;
import dev.folomkin.orderservice.domain.db.OrderJpaRepository;
import dev.folomkin.orderservice.external.PaymentHttpClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrderProcessor {

    private final OrderJpaRepository orderJpaRepository;
    private final OrderEntityMapper orderEntityMapper;
    private final PaymentHttpClient paymentHttpClient;
    private final KafkaTemplate<Long, OrderPaidEvent> kafkaTemplate;

    @Value("${order-paid-topic}")
    private String orderPaidTopic;

    public OrderEntity create(CreateOrderRequestDto request) {
        var entity = orderEntityMapper.toEntity(request);
        calculatePricingForOrder(entity);
        entity.setOrderStatus(dev.folomkin.api.http.order.OrderStatus.PENDING_PAYMENT);
        return orderJpaRepository.save(entity);
    }

    public OrderEntity getOrderOrThrow(Long id) {
        var orderItemEntityOptional = orderJpaRepository.findById(id);
        return orderItemEntityOptional
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id)));
    }

    private void calculatePricingForOrder(OrderEntity entity) {
        BigDecimal totalPrice = BigDecimal.ZERO;
        for (OrderItemEntity item : entity.getItems()) {
            var randomPrice = ThreadLocalRandom.current().nextDouble(100, 5000);
            item.setPriceAtPurchase(BigDecimal.valueOf(randomPrice));
            totalPrice = item.getPriceAtPurchase()
                    .multiply(BigDecimal.valueOf(item.getQuantity())
                            .add(totalPrice));
        }
        entity.setTotalAmount(totalPrice);
    }

    public OrderEntity processPayment(
            Long id,
            OrderPaymentRequest paymentRequest) {
        var entity = getOrderOrThrow(id);
        if (!entity.getOrderStatus().equals(OrderStatus.PENDING_PAYMENT)) {
            throw new RuntimeException("Order must be in status PENDING_PAYMENT");
        }
        var response = paymentHttpClient.createPayment(CreatePaymentRequestDto
                .builder()
                .orderId(id)
                .paymentMethod(paymentRequest.paymentMethod())
                .amount(entity.getTotalAmount())
                .build());
        var status = response.paymentStatus().equals(PaymentStatus.PAYMENT_SUCCEEDED)
                ? OrderStatus.PAYMENT_FAILED
                : OrderStatus.PAID;

        entity.setOrderStatus(status);
        saidOrderPaidEvent(entity, response);
        return orderJpaRepository.save(entity);
    }

    private void saidOrderPaidEvent(OrderEntity entity, CreatePaymentResponseDto responseDto) {
        kafkaTemplate.send(
                orderPaidTopic,
                entity.getId(),
                OrderPaidEvent.builder()
                        .orderId(entity.getId())
                        .amount(entity.getTotalAmount())
                        .paymentMethod(responseDto.paymentMethod())
                        .paymentId(responseDto.paymentId())
                        .build()
        ).thenAccept(
                resoult -> {
                    log.info("Order paid event sent with id {} ", entity.getId());
                }
        );
    }
}
