package ru.practicum.mystore.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.practicum.mystore.data.dto.OrderDto;
import ru.practicum.mystore.data.entity.Order;

public interface OrderService {
    Mono<Order> getOrCreateOrder(Long orderId);

    Mono<Order> getOrderWithItemsById(long orderId);

    Mono<Long> placeOrder(long orderId);

    Flux<OrderDto> getAllOrders();
}
