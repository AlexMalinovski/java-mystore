package ru.practicum.mystore.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.mystore.data.constant.OrderStatus;
import ru.practicum.mystore.data.dto.OrderDto;
import ru.practicum.mystore.data.entity.Order;
import ru.practicum.mystore.exception.NotFoundException;
import ru.practicum.mystore.repositories.OrderRepository;
import ru.practicum.mystore.service.OrderService;
import ru.practicum.mystore.service.mapper.OrderMapper;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public Order getOrCreateOrder(Long orderId) {
        return Optional.ofNullable(orderId)
                .flatMap(orderRepository::findById)
                .orElseGet(() -> orderRepository.save(
                        Order.builder().status(OrderStatus.NEW).build()));
    }

    @Override
    public Optional<Order> getOrderWithItemsById(long orderId) {
        return orderRepository.findOrderById(orderId);
    }

    @Override
    @Transactional
    public void placeOrder(long orderId) {
        Order order = orderRepository.findOrderById(orderId)
                .orElseThrow(() -> new NotFoundException("Заказ не может быть размещен"));
        if (order.getStatus() != OrderStatus.NEW) {
            throw new NotFoundException(String.format("Отсутствует новый заказ id=%s", orderId));
        }
        order.setStatus(OrderStatus.PLACED);
        log.info("Заказ номер {} размещён.", orderId);
    }

    @Override
    public List<OrderDto> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(orderMapper::toOrderDto)
                .toList();
    }
}
