package ru.practicum.mystore.basic.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.practicum.mystore.basic.data.dto.OrderDto;
import ru.practicum.mystore.basic.data.entity.Order;

public interface OrderService {
    Mono<Order> getOrCreateOrder(Long orderId, String login);

    Mono<Order> getOrderWithItemsById(long orderId);

    Mono<Long> placeOrder(long orderId);

    Flux<OrderDto> getAllOrders();
}
