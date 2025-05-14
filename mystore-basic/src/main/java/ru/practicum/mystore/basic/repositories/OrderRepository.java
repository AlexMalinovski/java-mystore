package ru.practicum.mystore.basic.repositories;

import jakarta.annotation.Nonnull;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.practicum.mystore.basic.data.entity.Order;

@Repository
public interface OrderRepository extends R2dbcRepository<Order, Long>, OrderRepositoryCustom {
    Mono<Order> findOrderById(Long id);

    @Nonnull
    Flux<Order> findAll();
}
