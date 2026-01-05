package dev.folomkin.deliveryservice.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryEntityRepository extends JpaRepository<DeliveryEntity, Long> {
}