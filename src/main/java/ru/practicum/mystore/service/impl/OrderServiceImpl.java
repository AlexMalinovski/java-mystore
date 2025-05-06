package ru.practicum.mystore.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.practicum.mystore.data.constant.OrderStatus;
import ru.practicum.mystore.data.dto.OrderDto;
import ru.practicum.mystore.data.entity.Item;
import ru.practicum.mystore.data.entity.Order;
import ru.practicum.mystore.data.entity.OrderItem;
import ru.practicum.mystore.exception.NotFoundException;
import ru.practicum.mystore.repositories.ItemRepository;
import ru.practicum.mystore.repositories.OrderItemRepository;
import ru.practicum.mystore.repositories.OrderRepository;
import ru.practicum.mystore.service.OrderService;
import ru.practicum.mystore.service.mapper.OrderMapper;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ItemRepository itemRepository;
    private final OrderMapper orderMapper;

    private Mono<Order> findOrderByIdFetchItems(Long id) {
        Mono<Map<Long, Item>> items = itemRepository.findAllById(
                        orderItemRepository.findAllByOrderId(id)
                                .map(OrderItem::getItemId))
                .collectMap(Item::getId, Function.identity());

        Mono<List<OrderItem>> orderItems = Flux.combineLatest(
                        items, orderItemRepository.findAllByOrderId(id), (i, oi) -> {
                            oi.setItem(i.get(oi.getItemId()));
                            return oi;
                        })
                .collectList();

        return orderRepository.findById(id)
                .zipWith(orderItems)
                .map(t -> {
                    Order order = t.getT1();
                    order.setOrderItems(t.getT2());
                    return order;
                });
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public Mono<Order> getOrCreateOrder(Long orderId) {

        return Mono.justOrEmpty(orderId)
                .flatMap(orderRepository::findById)
                .switchIfEmpty(orderRepository.save(
                        Order.builder().status(OrderStatus.NEW).build()));
    }

    @Override
    public Mono<Order> getOrderWithItemsById(long orderId) {
        return findOrderByIdFetchItems(orderId);
    }

    @Override
    @Transactional
    public Mono<Long> placeOrder(long orderId) {
        return findOrderByIdFetchItems(orderId)
                .switchIfEmpty(Mono.error(
                        new NotFoundException("Заказ не может быть размещен")))
                .filter(order -> order.getStatus() == OrderStatus.NEW)
                .switchIfEmpty(Mono.error(
                        new NotFoundException(String.format(
                                "Отсутствует новый заказ id=%s", orderId))))
                .doOnNext(order -> order.setStatus(OrderStatus.PLACED))
                .flatMap(orderRepository::save)
                .doOnNext(order -> log.info("Заказ номер {} размещён.", order.getId()))
                .map(Order::getId);
    }

    @Override
    public Flux<OrderDto> getAllOrders() {
        return orderRepository.findAllFetchItems()
                .map(orderMapper::toOrderDto);
    }
}
