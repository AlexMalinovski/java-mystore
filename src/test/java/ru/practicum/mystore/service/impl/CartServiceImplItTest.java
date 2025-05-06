package ru.practicum.mystore.service.impl;

import org.junit.jupiter.api.Test;
import ru.practicum.mystore.data.constant.OrderStatus;
import ru.practicum.mystore.data.dto.CartAction;
import ru.practicum.mystore.data.dto.MainItemDto;
import ru.practicum.mystore.service.AbstractServiceTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CartServiceImplItTest extends AbstractServiceTest {

    @Test
    void handleCartAction() {
        cartService.handleCartAction(new CartAction("plus", "source"), 1L, 1L);

        var actual = orderItemRepository.findByCompositeId(1L, 1L);
        actual.subscribe(val -> assertEquals(4, val.getItemQty()));
    }

    @Test
    void updateMainItemDtoByCart() {
        MainItemDto dto = MainItemDto.builder().id(1L).build();

        var actual = cartService.updateMainItemDtoByCart(dto, 1L);

        actual.subscribe(val -> {
            assertEquals(1L, val.getId());
            assertEquals(3, val.getCartCnt());
        });
    }

    @Test
    void getOrderCart() {
        var actual = cartService.getOrderCart(1L);

        actual.subscribe(val -> {
            assertEquals(1L, val.getId());
            assertEquals(OrderStatus.NEW, val.getStatus());
            assertEquals("14.0", val.getCost());
            assertEquals(2, val.getItems().size());
        });
    }
}