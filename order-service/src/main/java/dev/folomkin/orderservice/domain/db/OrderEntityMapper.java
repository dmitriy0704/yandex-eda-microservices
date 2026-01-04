package dev.folomkin.orderservice.domain.db;

import dev.folomkin.api.http.order.CreateOrderRequestDto;
import dev.folomkin.api.http.order.OrderDto;
import org.mapstruct.*;

@Mapper(
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING
)
public interface OrderEntityMapper {

    OrderEntity toEntity(CreateOrderRequestDto requestDto);

    @AfterMapping
    default void linkOrderItemEntities(@MappingTarget OrderEntity orderEntity) {
        orderEntity.getItems().forEach(orderItemEntity -> orderItemEntity.setOrder(orderEntity));
    }

    OrderDto toOrderDto(OrderEntity orderEntity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    OrderEntity partialUpdate(OrderDto orderDto, @MappingTarget OrderEntity orderEntity);
}