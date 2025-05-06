package ru.practicum.mystore.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.practicum.mystore.data.entity.Item;
import ru.practicum.mystore.data.entity.Order;
import ru.practicum.mystore.data.entity.OrderItem;
import ru.practicum.mystore.data.entity.id.OrderItemId;
import ru.practicum.mystore.repositories.OrderItemRepository;
import ru.practicum.mystore.service.OrderItemService;
import ru.practicum.mystore.service.mapper.OrderItemMapper;

import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderItemServiceImpl implements OrderItemService {
    private final OrderItemRepository orderItemRepository;
    private final OrderItemMapper orderItemMapper;

    private OrderItem incrementItemCnt(OrderItem orderItem) {
        Integer curCnt = Optional.ofNullable(orderItem.getItemQty()).orElse(0);
        orderItem.setItemQty(curCnt + 1);
        return orderItem;
    }

    private OrderItem decrementItemCnt(OrderItem orderItem) {
        int newCnt = Math.max(0, orderItem.getItemQty() - 1);
        orderItem.setItemQty(newCnt);
        return orderItem;
    }

    private Mono<OrderItem> createOrderItem(Order order, Item item) {
        return orderItemRepository.save(orderItemMapper.toOrderItem(order, item));
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
    public Mono<Void> addOrderItem(Order order, Item item) {
        return Mono.just(new OrderItemId(order.getId(), item.getId()))
                .flatMap(params -> orderItemRepository.findByCompositeId(params.getOrderId(), params.getItemId()))
                .switchIfEmpty(createOrderItem(order, item))
                .map(this::incrementItemCnt)
                .doOnNext(orderItem -> updateOrderItemByItem(orderItem, item))
                .flatMap(orderItemRepository::save)
                .then();
    }

    @Override
    @Transactional
    public Mono<Void> removeOrderItem(Order order, Item item) {
        return Mono.just(new OrderItemId(order.getId(), item.getId()))
                .flatMap(params -> orderItemRepository.findByCompositeId(params.getOrderId(), params.getItemId()))
                .doOnNext(this::decrementItemCnt)
                .doOnNext(orderItem -> {
                    if (orderItem.getItemQty() > 0) {
                        updateOrderItemByItem(orderItem, item);
                    }
                })
                .flatMap(orderItemRepository::save)
                .then();
    }

    @Override
    public Flux<OrderItem> findByOrderId(long orderId) {
        return orderItemRepository.findAllByOrderId(orderId);
    }

    @Override
    @Transactional
    public Mono<Void> removeOrderItemFull(Order order, Item item) {
        return Mono.just(new OrderItemId(order.getId(), item.getId()))
                .flatMap(params -> orderItemRepository.findByCompositeId(params.getOrderId(), params.getItemId()))
                .doOnNext(orderItem -> orderItem.setItemQty(0))
                .flatMap(orderItemRepository::save)
                .then();
    }

}
