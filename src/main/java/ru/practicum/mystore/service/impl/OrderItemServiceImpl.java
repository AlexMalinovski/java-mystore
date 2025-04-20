package ru.practicum.mystore.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.mystore.data.entity.Item;
import ru.practicum.mystore.data.entity.Order;
import ru.practicum.mystore.data.entity.OrderItem;
import ru.practicum.mystore.data.entity.id.OrderItemId;
import ru.practicum.mystore.repositories.OrderItemRepository;
import ru.practicum.mystore.service.OrderItemService;
import ru.practicum.mystore.service.mapper.OrderItemMapper;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderItemServiceImpl implements OrderItemService {
    private final OrderItemRepository orderItemRepository;
    private final OrderItemMapper orderItemMapper;

    private OrderItem incrementItemCnt(OrderItem orderItem) {
        orderItem.setItemQty(orderItem.getItemQty() + 1);
        return orderItem;
    }

    private OrderItem decrementItemCnt(OrderItem orderItem) {
        int newCnt = Math.max(0, orderItem.getItemQty() - 1);
        orderItem.setItemQty(newCnt);
        return orderItem;
    }

    private OrderItem createOrderItem(Order order, Item item) {
        OrderItem orderItem = orderItemMapper.toOrderItem(order, item);
        return orderItemRepository.save(orderItem);
    }

    private OrderItem getDetachedOrderItem(OrderItem orderItem) {
        return orderItem.toBuilder().build();
    }

    private void handleItemPriceChanged(OrderItem orderItem) {
        log.info("Цена товара id={} в заказе id={} изменилась", orderItem.getItemId(), orderItem.getOrderId());
    }

    private void updateOrderItemByItem(OrderItem existOrderItem, Item item) {
        if (!Objects.equals(item.getPrice(), existOrderItem.getItemPrice())) {
            handleItemPriceChanged(getDetachedOrderItem(existOrderItem));
            existOrderItem.setItemPrice(item.getPrice());
        }
    }

    @Override
    @Transactional
    public void addOrderItem(Order order, Item item) {
        final var orderItemId = new OrderItemId(order.getId(), item.getId());
        OrderItem existOrderItem = orderItemRepository.findById(orderItemId)
                .map(this::incrementItemCnt)
                .orElseGet(() -> createOrderItem(order, item));
        updateOrderItemByItem(existOrderItem, item);
    }

    @Override
    @Transactional
    public void removeOrderItem(Order order, Item item) {
        final var orderItemId = new OrderItemId(order.getId(), item.getId());
        orderItemRepository.findById(orderItemId)
                .map(this::decrementItemCnt)
                .filter(orderItem -> orderItem.getItemQty() > 0)
                .ifPresent(orderItem -> updateOrderItemByItem(orderItem, item));
    }

    @Override
    public List<OrderItem> findByOrderId(long orderId) {
        return orderItemRepository.findAllByOrderId(orderId);
    }

    @Override
    @Transactional
    public void removeOrderItemFull(Order order, Item item) {
        final var orderItemId = new OrderItemId(order.getId(), item.getId());
        orderItemRepository.findById(orderItemId)
                .ifPresent(orderItem -> orderItem.setItemQty(0));
    }

}
