package dev.folomkin.deliveryservice.kafka;

import dev.folomkin.api.kafka.OrderPaidEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;

@Slf4j
@EnableKafka
@Configuration
public class OrderPaidKafkaConsumer {

    @KafkaListener
    public void listen(OrderPaidEvent event) {
        log.info("Received order paid event: {}", event);
    }
}
