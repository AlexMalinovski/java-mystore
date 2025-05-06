package ru.practicum.mystore.repositories;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.practicum.mystore.data.entity.OrderItem;

@Repository
public interface OrderItemRepository extends R2dbcRepository<OrderItem, Long> {
    Flux<OrderItem> findAllByOrderId(long orderId);

    @Query("SELECT * FROM orders_items oi WHERE oi.order_id = :orderId AND oi.item_id = :itemId")
    Mono<OrderItem> findByCompositeId(
            @Param("orderId") Long orderId,
            @Param("itemId") Long itemId);
}
