package ru.practicum.mystore.service.impl;

import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.mystore.Util;
import ru.practicum.mystore.data.constant.OrderStatus;
import ru.practicum.mystore.data.dto.OrderDto;
import ru.practicum.mystore.data.entity.Order;
import ru.practicum.mystore.service.AbstractServiceTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OrderServiceImplItTest extends AbstractServiceTest {

    @Test
    @Transactional
    void getOrCreateOrder_whenOrderExist_thenReturnThis() {
        Order order = orderRepository.save(Util.aOrder());

        var actual = orderService.getOrCreateOrder(order.getId());

        assertEquals(order, actual);
    }

    @Test
    @Transactional
    void getOrCreateOrder_whenOrderNotExist_thenCreate() {
        var actual = orderService.getOrCreateOrder(9999L);

        assertTrue(orderRepository.existsById(actual.getId()));
    }


    @Test
    @Transactional
    void getOrderWithItemsById() {
        var actual = orderService.getOrderWithItemsById(1L).get();

        var detached = actual.toBuilder().build();

        assertEquals("item2", detached.getOrderItems().get(0).getItem().getName());
    }

    @Test
    @Transactional
    void placeOrder() {
        orderService.placeOrder(1L);

        Order order = orderRepository.findOrderById(1L).get();
        assertEquals(OrderStatus.PLACED, order.getStatus());
    }

    @Test
    void getAllOrders() {
        List<OrderDto> orders = orderService.getAllOrders();

        assertEquals(1, orders.size());
        assertEquals(2, orders.get(0).getItemsSummary().size());
    }
}