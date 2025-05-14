package ru.practicum.mystore.basic.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.practicum.mystore.basic.data.entity.Item;
import ru.practicum.mystore.basic.data.entity.Order;
import ru.practicum.mystore.basic.data.entity.OrderItem;

public interface OrderItemService {
    Mono<Void> addOrderItem(Order order, Item item);

    Mono<Void> removeOrderItem(Order order, Item item);

    Flux<OrderItem> findByOrderId(long orderId);

    Mono<Void> removeOrderItemFull(Order order, Item item);
}
