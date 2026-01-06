package dev.folomkin.deliveryservice.domain;

import dev.folomkin.api.kafka.DeliveryAssignedEvent;
import dev.folomkin.api.kafka.OrderPaidEvent;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeliveryProcessor {

    private final DeliveryEntityRepository repository;
    private final KafkaTemplate<Long, DeliveryAssignedEvent> kafkaTemplate;

    @Value("${delivery-assigned-topic}")
    private String deliveryAssignedTopic;

    public void processOrderPaid(OrderPaidEvent event) {
        var orderId = event.orderId();
        var found = repository.findByOrderId(orderId);
        if (found.isPresent()) {
            log.info("Found order delivery was already assigned: delivery={}", found.get());
        }
        var assignedDelivery = assignedDelivery(orderId);
        sendDeliveryAssignedEvent(assignedDelivery);
    }



    private DeliveryEntity assignedDelivery(Long orderId) {
        var entity = new DeliveryEntity();
        entity.setOrderId(orderId);
        entity.setCourierName("courier-" + ThreadLocalRandom.current().nextInt(100));
        entity.setEtaMinutes(ThreadLocalRandom.current().nextInt(10, 45));
        log.info("Saved order delivery was assigned: delivery={}", entity);
        return repository.save(entity);
    }


    private void sendDeliveryAssignedEvent(DeliveryEntity assignedDelivery) {
        kafkaTemplate.send(
                deliveryAssignedTopic,
                assignedDelivery.getOrderId(),
                DeliveryAssignedEvent.builder()
                        .courierName(assignedDelivery.getCourierName())
                        .orderId(assignedDelivery.getOrderId())
                        .etaMinutes(assignedDelivery.getEtaMinutes())
                        .build()
        ).thenAccept(
                result ->
                        log.info("Delivery assigned event send deliveryId={}", assignedDelivery.getId())
        );
    }
}
