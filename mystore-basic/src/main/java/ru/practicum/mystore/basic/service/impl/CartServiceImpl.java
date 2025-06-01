package ru.practicum.mystore.basic.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import ru.practicum.mystore.basic.data.dto.CartAction;
import ru.practicum.mystore.basic.data.dto.CartDto;
import ru.practicum.mystore.basic.data.dto.MainItemDto;
import ru.practicum.mystore.basic.data.entity.Item;
import ru.practicum.mystore.basic.data.entity.Order;
import ru.practicum.mystore.basic.data.entity.OrderItem;
import ru.practicum.mystore.basic.exception.BadRequestException;
import ru.practicum.mystore.basic.exception.NotFoundException;
import ru.practicum.mystore.basic.service.CartService;
import ru.practicum.mystore.basic.service.ItemService;
import ru.practicum.mystore.basic.service.OrderItemService;
import ru.practicum.mystore.basic.service.OrderService;
import ru.practicum.mystore.basic.service.mapper.ItemMapper;
import ru.practicum.mystore.basic.service.mapper.OrderItemMapper;
import ru.practicum.mystore.common.payment.client.BalanceControllerApi;
import ru.practicum.mystore.common.payment.data.dto.BalanceDto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
@Slf4j
public class CartServiceImpl implements CartService {
    private final OrderService orderService;
    private final OrderItemService orderItemService;
    private final ItemService itemService;
    private final ItemMapper itemMapper;
    private final OrderItemMapper orderItemMapper;
    private final BalanceControllerApi balanceControllerApiClient;

    private BiFunction<Order, Item, Mono<Void>> changeCartAction(CartAction cartAction) {
        return switch (cartAction.getAction().toLowerCase(Locale.ROOT)) {
            case "plus" -> orderItemService::addOrderItem;
            case "minus" -> orderItemService::removeOrderItem;
            case "delete" -> orderItemService::removeOrderItemFull;
            default -> throw new IllegalArgumentException("Неподдерживаемое событие корзины");
        };
    }

    @Override
    @Transactional
    public Mono<Long> handleCartAction(CartAction cartAction, long itemId, Long orderId) {
        Mono<Order> order = orderService.getOrCreateOrder(orderId);
        Mono<Item> item = itemService.findById(itemId)
                .switchIfEmpty(
                        Mono.error(new BadRequestException(String.format("Товар id=%d не найден в БД", itemId))));

        return order.zipWith(item)
                .doOnSuccess(t -> changeCartAction(cartAction).apply(t.getT1(), t.getT2()).subscribe())
                .map(t -> t.getT1().getId());
    }

    private Mono<Map<Long, OrderItem>> getOrderItems(long orderId) {
        return orderItemService.findByOrderId(orderId)
                .filter(orderItem -> orderItem.getItemQty() > 0)
                .collectMap(OrderItem::getItemId, Function.identity());
    }

    @Override
    public Mono<Page<MainItemDto>> updatePageDataByCart(Page<MainItemDto> pageData, Long orderId) {
        return getOrderItems(orderId)
                .map(cart -> pageData.map(mainItemDto -> itemMapper.updateMainItemDto(
                        mainItemDto.toBuilder(), cart.get(mainItemDto.getId()))));
    }

    @Override
    public Mono<MainItemDto> updateMainItemDtoByCart(MainItemDto mainItemDto, Long orderId) {
        return getOrderItems(orderId)
                .map(items -> itemMapper.updateMainItemDto(
                        mainItemDto.toBuilder(), items.get(mainItemDto.getId())));
    }

    @Override
    public Mono<CartDto> getOrderCart(Long orderId) {
        if (orderId == null) {
            throw new NotFoundException("Корзина не найдена. Добавьте товары.");
        }
        return orderService.getOrderWithItemsById(orderId)
                .switchIfEmpty(Mono.error(new NotFoundException("Корзина не найдена")))
                .map(order -> order.toBuilder()
                        .orderItems(filterQuantityPositive(order.getOrderItems()))
                        .build())
                .map(orderItemMapper::toCartDto)


                .zipWith(balanceControllerApiClient.getBalance()
                        .doOnError(e -> log.error("Ошибка при работе с сервисом платежей:{}", e.getMessage(), e))
                        .onErrorReturn(new BalanceDto()))
                .map(t -> setPaymentAvailable(t.getT1(), t.getT2()));
    }

    private CartDto setPaymentAvailable(CartDto cartDto, BalanceDto balanceDto) {
        Optional<BigDecimal> valOptional = Optional.ofNullable(balanceDto)
                .map(BalanceDto::getValue);
        boolean isPayment = valOptional
                .map(val -> val.compareTo(new BigDecimal(cartDto.getCost())) >= 0)
                .orElse(false);

        final String message;
        if (valOptional.isPresent()) {
            message = !isPayment ? "Недостаточно средств" : "";
        } else {
            message = "Сервис платежей недоступен.";
        }
        return cartDto.toBuilder()
                .isAvailablePayment(isPayment)
                .systemMessage(message)
                .build();
    }

    private List<OrderItem> filterQuantityPositive(List<OrderItem> orderItems) {
        return orderItems.stream().filter(orderItem -> orderItem.getItemQty() > 0).toList();
    }
}
