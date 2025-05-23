package ru.practicum.mystore.basic.repositories;

import reactor.core.publisher.Flux;
import ru.practicum.mystore.basic.data.entity.Order;

public interface OrderRepositoryCustom {

    Flux<Order> findAllFetchItems();
}
