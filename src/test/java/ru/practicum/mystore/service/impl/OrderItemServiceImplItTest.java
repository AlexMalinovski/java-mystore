package ru.practicum.mystore.service.impl;

import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.mystore.data.entity.Item;
import ru.practicum.mystore.data.entity.Order;
import ru.practicum.mystore.data.entity.OrderItem;
import ru.practicum.mystore.data.entity.id.OrderItemId;
import ru.practicum.mystore.service.AbstractServiceTest;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OrderItemServiceImplItTest extends AbstractServiceTest {

    @Test
    @Transactional
    void addOrderItem() {
        Item item = itemRepository.findById(3L).get();
        Order order = Order.builder().id(1L).build();

        orderItemService.addOrderItem(order, item);

        order = orderRepository.findOrderById(1L).get();
        assertEquals(3, order.getOrderItems().size());
    }

    @Test
    @Transactional
    void removeOrderItem() {
        Order order = orderRepository.findOrderById(1L).get();
        Item item = itemRepository.findById(1L).get();

        orderItemService.removeOrderItem(order, item);

        var actual = orderItemRepository.findById(new OrderItemId(1L, 1L)).get();
        assertEquals(2, actual.getItemQty());
    }

    @Test
    void findByOrderId() {
        List<OrderItem> actual = orderItemService.findByOrderId(1L);

        assertEquals(2, actual.size());
        Map<Long, OrderItem> byItemId = actual.stream()
                .collect(Collectors.toMap(OrderItem::getItemId, Function.identity()));
        assertTrue(byItemId.containsKey(1L));
        assertTrue(byItemId.containsKey(2L));
    }

    @Test
    @Transactional
    void removeOrderItemFull() {
        Order order = orderRepository.findOrderById(1L).get();
        Item item = itemRepository.findById(1L).get();

        orderItemService.removeOrderItemFull(order, item);

        var actual = orderItemRepository.findById(new OrderItemId(1L, 1L)).get();
        assertEquals(0, actual.getItemQty());
    }
}