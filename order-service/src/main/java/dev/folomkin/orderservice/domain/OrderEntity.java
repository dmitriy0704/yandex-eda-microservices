package dev.folomkin.orderservice.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table("orders")
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_entity_gen")
    @SequenceGenerator(name = "order_entity_gen", sequenceName = "order_entity_seq")
    @Column(name = "id", nullable = false)
    private Long id;


    private Long customerId;

    private String address;



}
