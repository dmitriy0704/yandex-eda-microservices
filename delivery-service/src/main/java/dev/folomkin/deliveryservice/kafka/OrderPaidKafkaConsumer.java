package dev.folomkin.deliveryservice.kafka;

import dev.folomkin.api.kafka.OrderPaidEvent;
import dev.folomkin.deliveryservice.domain.DeliveryEntity;
import dev.folomkin.deliveryservice.domain.DeliveryEntityRepository;
import dev.folomkin.deliveryservice.domain.DeliveryProcessor;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;

import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@EnableKafka
@Configuration
@AllArgsConstructor
public class OrderPaidKafkaConsumer {

    private final DeliveryProcessor deliveryProcessor;

    @KafkaListener(
            topics = "${order-paid-topic}",
            containerFactory = "orderPaidEventListenerFactory"

    )
    public void listen(OrderPaidEvent event) {
        log.info("Received order paid event: {}", event);
        deliveryProcessor.processOrderPaid(event);
    }
}
