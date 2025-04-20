package ru.practicum.mystore.service;

import ru.practicum.mystore.data.entity.Item;
import ru.practicum.mystore.data.entity.Order;
import ru.practicum.mystore.data.entity.OrderItem;

import java.util.List;

public interface OrderItemService {
    void addOrderItem(Order order, Item item);

    void removeOrderItem(Order order, Item item);

    List<OrderItem> findByOrderId(long orderId);

    void removeOrderItemFull(Order order, Item item);
}
