package ru.practicum.mystore.service;

import org.springframework.data.domain.Page;
import ru.practicum.mystore.data.dto.CartAction;
import ru.practicum.mystore.data.dto.CartDto;
import ru.practicum.mystore.data.dto.MainItemDto;

public interface CartService {
    Long handleCartAction(CartAction cartAction, long itemId, Long orderId);

    Page<MainItemDto> updatePageDataByCart(Page<MainItemDto> pageData, Long orderId);

    MainItemDto updateMainItemDtoByCart(MainItemDto pageData, Long orderId);

    CartDto getOrderCart(Long orderId);
}
