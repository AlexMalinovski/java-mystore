package ru.practicum.mystore.service;

import org.springframework.data.domain.Page;
import reactor.core.publisher.Mono;
import ru.practicum.mystore.data.dto.CartAction;
import ru.practicum.mystore.data.dto.CartDto;
import ru.practicum.mystore.data.dto.MainItemDto;

public interface CartService {
    Mono<Long> handleCartAction(CartAction cartAction, long itemId, Long orderId);

    Mono<Page<MainItemDto>> updatePageDataByCart(Page<MainItemDto> pageData, Long orderId);

    Mono<MainItemDto> updateMainItemDtoByCart(MainItemDto pageData, Long orderId);

    Mono<CartDto> getOrderCart(Long orderId);
}
