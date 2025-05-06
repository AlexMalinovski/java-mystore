package ru.practicum.mystore.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.practicum.mystore.data.entity.Item;
import ru.practicum.mystore.data.entity.Order;
import ru.practicum.mystore.data.entity.OrderItem;

public interface OrderItemService {
    Mono<Void> addOrderItem(Order order, Item item);

    Mono<Void> removeOrderItem(Order order, Item item);

    Flux<OrderItem> findByOrderId(long orderId);

    Mono<Void> removeOrderItemFull(Order order, Item item);
}
