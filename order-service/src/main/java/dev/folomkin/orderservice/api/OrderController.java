package dev.folomkin.orderservice.api;

import dev.folomkin.orderservice.domain.OrderEntity;
import dev.folomkin.orderservice.domain.OrderEntityMapper;
import dev.folomkin.orderservice.domain.OrderItemEntity;
import dev.folomkin.orderservice.domain.OrderProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderProcessor orderProcessor;
    private final OrderEntityMapper orderEntityMapper;

    @PostMapping
    public OrderDto create(@RequestBody OrderEntity orderItemEntity) {
        log.info("creating order with id: {}", orderItemEntity);
        var saved = orderProcessor.create(orderItemEntity);
        return orderEntityMapper.toOrderDto(saved);
    }

    @GetMapping("/{id}")
    public OrderDto getOne(@PathVariable Long id) {
        log.info("Received order with id: {}", id);
        var found = orderProcessor.getOrderOrThrow(id);
        return orderEntityMapper.toOrderDto(found);
    }
}
