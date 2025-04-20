package ru.practicum.mystore.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.mystore.data.dto.CartAction;
import ru.practicum.mystore.data.dto.CartDto;
import ru.practicum.mystore.data.dto.MainItemDto;
import ru.practicum.mystore.data.entity.Item;
import ru.practicum.mystore.data.entity.Order;
import ru.practicum.mystore.data.entity.OrderItem;
import ru.practicum.mystore.exception.BadRequestException;
import ru.practicum.mystore.exception.NotFoundException;
import ru.practicum.mystore.service.CartService;
import ru.practicum.mystore.service.ItemService;
import ru.practicum.mystore.service.OrderItemService;
import ru.practicum.mystore.service.OrderService;
import ru.practicum.mystore.service.mapper.ItemMapper;
import ru.practicum.mystore.service.mapper.OrderItemMapper;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final OrderService orderService;
    private final OrderItemService orderItemService;
    private final ItemService itemService;
    private final ItemMapper itemMapper;
    private final OrderItemMapper orderItemMapper;

    @Override
    @Transactional
    public Long handleCartAction(CartAction cartAction, long itemId, Long orderId) {
        Order order = orderService.getOrCreateOrder(orderId);
        Item item = itemService.findById(itemId)
                .orElseThrow(
                        () -> new BadRequestException(String.format("Товар id=%d не найден в БД", itemId)));
        switch (cartAction.getAction().toLowerCase(Locale.ROOT)) {
            case "plus" -> orderItemService.addOrderItem(order, item);
            case "minus" -> orderItemService.removeOrderItem(order, item);
            case "delete" -> orderItemService.removeOrderItemFull(order, item);
            default -> throw new IllegalArgumentException("Неподдерживаемое событие корзины");
        }
        return order.getId();
    }

    private Map<Long, OrderItem> getOrderItems(long orderId) {
        return orderItemService.findByOrderId(orderId)
                .stream()
                .filter(orderItem -> orderItem.getItemQty() > 0)
                .collect(Collectors.toMap(OrderItem::getItemId, Function.identity()));
    }

    @Override
    public Page<MainItemDto> updatePageDataByCart(Page<MainItemDto> pageData, Long orderId) {
        final Map<Long, OrderItem> cartByItemId = getOrderItems(orderId);
        return pageData.map(
                mainItemDto -> itemMapper.updateMainItemDto(
                        mainItemDto.toBuilder(), cartByItemId.get(mainItemDto.getId())));
    }

    @Override
    public MainItemDto updateMainItemDtoByCart(MainItemDto mainItemDto, Long orderId) {
        final Map<Long, OrderItem> cartByItemId = getOrderItems(orderId);
        return itemMapper.updateMainItemDto(
                mainItemDto.toBuilder(), cartByItemId.get(mainItemDto.getId()));
    }

    @Override
    public CartDto getOrderCart(Long orderId) {
        Order order = orderService.getOrderWithItemsById(orderId)
                .orElseThrow(() -> new NotFoundException("Корзина не найдена"));

        List<OrderItem> items = order.getOrderItems().stream()
                .filter(item -> item.getItemQty() > 0)
                .toList();
        return orderItemMapper.toCartDto(
                order.toBuilder().orderItems(items).build());
    }
}
