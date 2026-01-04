package dev.folomkin.orderservice.domain.db;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "orders")
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_entity_gen")
    @SequenceGenerator(name = "order_entity_gen", sequenceName = "orderentity_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "customer_id")
    private Long customerId;

    @Column(name = "address")
    private String address;

    @Column(name = "total_amount", precision = 19, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "courier_name")
    private String courierName;

    @Column(name = "eta_minutes")
    private Integer etaMinutes;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status", nullable = false)
    private dev.folomkin.api.http.order.OrderStatus orderStatus;


    @OneToMany(mappedBy = "order", cascade = CascadeType.PERSIST)
    private Set<OrderItemEntity> items = new LinkedHashSet<>();

}
