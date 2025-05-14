package ru.practicum.mystore.basic.repositories.impl;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import reactor.core.publisher.Flux;
import ru.practicum.mystore.basic.data.constant.OrderStatus;
import ru.practicum.mystore.basic.data.entity.Item;
import ru.practicum.mystore.basic.data.entity.Order;
import ru.practicum.mystore.basic.data.entity.OrderItem;
import ru.practicum.mystore.basic.repositories.OrderRepositoryCustom;

import java.util.ArrayList;

@RequiredArgsConstructor
public class OrderRepositoryCustomImpl implements OrderRepositoryCustom {

    private final R2dbcEntityTemplate template;

    @Builder
    private record QueryResult(
            Long id, OrderStatus status, Integer itemQty, Long itemPrice, Long itemId, String itemName) {
    }

    private Order toOrder(QueryResult qr) {
        return Order.builder()
                .id(qr.id)
                .status(qr.status)
                .orderItems(new ArrayList<>())
                .build();
    }

    private OrderItem toOrderItem(QueryResult qr) {
        return OrderItem.builder()
                .orderId(qr.id)
                .itemId(qr.itemId)
                .itemQty(qr.itemQty)
                .itemPrice(qr.itemPrice)
                .item(Item.builder()
                        .name(qr.itemName)
                        .build())
                .build();
    }

    @Override
    public Flux<Order> findAllFetchItems() {
        return template.getDatabaseClient().sql(
                        "SELECT o.id, o.status, oi.item_qty, oi.item_price, oi.item_id, i.name " +
                                "FROM orders o LEFT JOIN orders_items oi ON o.id = oi.order_id " +
                                "LEFT JOIN items i ON oi.item_id = i.id WHERE oi.item_qty > 0")
                .map(((row, rowMetadata) -> QueryResult.builder()
                        .id(row.get("id", Long.class))
                        .status(OrderStatus.valueOf(row.get("status", String.class)))
                        .itemQty(row.get("item_qty", Integer.class))
                        .itemPrice(row.get("item_price", Long.class))
                        .itemId(row.get("item_id", Long.class))
                        .itemName(row.get("name", String.class))
                        .build()))
                .all()
                .groupBy(this::toOrder, this::toOrderItem)
                .flatMap(groupedFlux -> groupedFlux.collectList()
                        .map(list -> {
                            Order order = groupedFlux.key();
                            order.setOrderItems(list);
                            return order;
                        }));
    }
}
