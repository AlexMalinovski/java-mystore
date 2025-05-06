package ru.practicum.mystore.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.practicum.mystore.Util;
import ru.practicum.mystore.data.constant.OrderStatus;
import ru.practicum.mystore.data.entity.Order;
import ru.practicum.mystore.service.AbstractServiceTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OrderServiceImplItTest extends AbstractServiceTest {

    @Test
    void getOrCreateOrder_whenOrderExist_thenReturnThis() {
        Order order = orderRepository.save(Util.aOrder()).block();

        orderService.getOrCreateOrder(order.getId()).subscribe(actual -> {
            assertEquals(order, actual);
        });
    }

    @Test
    void getOrCreateOrder_whenOrderNotExist_thenCreate() {
        var order = orderService.getOrCreateOrder(9999L).block();

        orderRepository.existsById(order.getId()).subscribe(Assertions::assertTrue);
    }

    @Test
    void getOrderWithItemsById() {
        orderService.getOrderWithItemsById(1L).subscribe(actual -> {
            assertEquals("item2", actual.getOrderItems().get(0).getItem().getName());
        });
    }

    @Test
    void placeOrder() {
        orderService.placeOrder(1L).subscribe();

        orderRepository.findOrderById(1L).subscribe(order -> {
            assertEquals(OrderStatus.PLACED, order.getStatus());
        });
    }

    @Test
    void getAllOrders() {
        orderService.getAllOrders()
                .collectList()
                .subscribe(orders -> {
                    assertEquals(1, orders.size());
                    assertEquals(2, orders.get(0).getItemsSummary().size());
                });
    }
}