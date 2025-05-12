package ru.practicum.mystore.repositories;

import reactor.core.publisher.Flux;
import ru.practicum.mystore.data.entity.Order;

public interface OrderRepositoryCustom {

    Flux<Order> findAllFetchItems();
}
