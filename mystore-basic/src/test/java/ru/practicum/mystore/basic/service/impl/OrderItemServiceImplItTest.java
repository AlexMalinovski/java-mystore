package ru.practicum.mystore.basic.service.impl;

import org.junit.jupiter.api.Test;
import ru.practicum.mystore.basic.data.entity.Item;
import ru.practicum.mystore.basic.data.entity.Order;
import ru.practicum.mystore.basic.data.entity.OrderItem;
import ru.practicum.mystore.basic.service.AbstractServiceTest;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OrderItemServiceImplItTest extends AbstractServiceTest {

    @Test
    void addOrderItem() {
        Item item = itemRepository.findById(3L).block();
        Order order = Order.builder().id(1L).build();

        orderItemService.addOrderItem(order, item).subscribe();

        orderRepository.findOrderById(1L).subscribe(actual -> {
            assertEquals(3, actual.getOrderItems().size());
        });
    }

    @Test
    void removeOrderItem() {
        Order order = orderRepository.findOrderById(1L).block();
        Item item = itemRepository.findById(1L).block();

        orderItemService.removeOrderItem(order, item).subscribe();

        orderItemRepository.findByCompositeId(1L, 1L).subscribe(actual -> {
            assertEquals(2, actual.getItemQty());
        });

    }

    @Test
    void findByOrderId() {
        orderItemService.findByOrderId(1L).collectList().subscribe(actual -> {
            assertEquals(2, actual.size());
            Map<Long, OrderItem> byItemId = actual.stream()
                    .collect(Collectors.toMap(OrderItem::getItemId, Function.identity()));
            assertTrue(byItemId.containsKey(1L));
            assertTrue(byItemId.containsKey(2L));
        });
    }

    @Test
    void removeOrderItemFull() {
        Order order = orderRepository.findOrderById(1L).block();
        Item item = itemRepository.findById(1L).block();

        orderItemService.removeOrderItemFull(order, item).subscribe();

        orderItemRepository.findByCompositeId(1L, 1L).subscribe(actual -> {
            assertEquals(0, actual.getItemQty());
        });
    }
}