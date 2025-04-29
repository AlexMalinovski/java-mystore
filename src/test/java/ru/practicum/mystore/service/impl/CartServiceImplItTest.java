package ru.practicum.mystore.service.impl;

import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.mystore.data.constant.OrderStatus;
import ru.practicum.mystore.data.dto.CartAction;
import ru.practicum.mystore.data.dto.CartDto;
import ru.practicum.mystore.data.dto.MainItemDto;
import ru.practicum.mystore.data.entity.id.OrderItemId;
import ru.practicum.mystore.service.AbstractServiceTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CartServiceImplItTest extends AbstractServiceTest {

    @Test
    @Transactional
    void handleCartAction() {
        cartService.handleCartAction(new CartAction("plus", "source"), 1L, 1L);

        var actual = orderItemRepository.findById(new OrderItemId(1L, 1L)).get();
        assertEquals(4, actual.getItemQty());
    }

    @Test
    void updateMainItemDtoByCart() {
        MainItemDto dto = MainItemDto.builder().id(1L).build();

        MainItemDto actual = cartService.updateMainItemDtoByCart(dto, 1L);

        assertEquals(1L, actual.getId());
        assertEquals(3, actual.getCartCnt());
    }

    @Test
    void getOrderCart() {
        CartDto actual = cartService.getOrderCart(1L);

        assertEquals(1L, actual.getId());
        assertEquals(OrderStatus.NEW, actual.getStatus());
        assertEquals("14.0", actual.getCost());
        assertEquals(2, actual.getItems().size());
    }
}