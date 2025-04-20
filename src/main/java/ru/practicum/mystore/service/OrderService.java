package ru.practicum.mystore.service;

import ru.practicum.mystore.data.dto.OrderDto;
import ru.practicum.mystore.data.entity.Order;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    Order getOrCreateOrder(Long orderId);

    Optional<Order> getOrderWithItemsById(long orderId);

    void placeOrder(long orderId);

    List<OrderDto> getAllOrders();
}
