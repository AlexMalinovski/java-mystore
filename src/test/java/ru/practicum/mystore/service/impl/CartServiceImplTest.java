package ru.practicum.mystore.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import ru.practicum.mystore.data.dto.CartAction;
import ru.practicum.mystore.data.dto.MainItemDto;
import ru.practicum.mystore.data.entity.Item;
import ru.practicum.mystore.data.entity.Order;
import ru.practicum.mystore.data.entity.OrderItem;
import ru.practicum.mystore.service.ItemService;
import ru.practicum.mystore.service.OrderItemService;
import ru.practicum.mystore.service.OrderService;
import ru.practicum.mystore.service.mapper.ItemMapper;
import ru.practicum.mystore.service.mapper.OrderItemMapper;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CartServiceImplTest {
//    @Mock
//    private OrderService orderService;
//    @Mock
//    private OrderItemService orderItemService;
//    @Mock
//    private ItemService itemService;
//    @Mock
//    private ItemMapper itemMapper;
//    @Mock
//    private OrderItemMapper orderItemMapper;
//    @InjectMocks
//    private CartServiceImpl cartService;
//
//    @ParameterizedTest
//    @ValueSource(strings = {"plus", "minus", "delete"})
//    void handleCartAction(String action) {
//        Order order = Order.builder().id(2L).build();
//        Item item = Item.builder().id(1L).build();
//        CartAction cartAction = new CartAction(action, "/source");
//        when(orderService.getOrCreateOrder(anyLong())).thenReturn(order);
//        when(itemService.findById(anyLong())).thenReturn(Optional.of(item));
//
//        Long actual = cartService.handleCartAction(cartAction, item.getId(), order.getId());
//
//        switch (action) {
//            case "plus" -> verify(orderItemService).addOrderItem(order, item);
//            case "minus" -> verify(orderItemService).removeOrderItem(order, item);
//            case "delete" -> verify(orderItemService).removeOrderItemFull(order, item);
//        }
//        assertEquals(order.getId(), actual);
//    }
//
//    @Test
//    void updatePageDataByCart() {
//        Page<MainItemDto> pageData = Mockito.mock(Page.class);
//        OrderItem orderItem = OrderItem.builder().orderId(1L).itemId(2L).itemQty(1).build();
//        when(orderItemService.findByOrderId(anyLong())).thenReturn(List.of(orderItem));
//
//        cartService.updatePageDataByCart(pageData, orderItem.getOrderId());
//
//        verify(pageData).map(any());
//    }
//
//    @Test
//    void updateMainItemDtoByCart() {
//        MainItemDto mainItemDto = MainItemDto.builder().id(100L).build();
//        OrderItem orderItem = OrderItem.builder().orderId(1L).itemId(mainItemDto.getId()).itemQty(1).build();
//        when(orderItemService.findByOrderId(anyLong())).thenReturn(List.of(orderItem));
//
//        cartService.updateMainItemDtoByCart(mainItemDto, orderItem.getOrderId());
//
//        var captor = ArgumentCaptor.forClass(MainItemDto.MainItemDtoBuilder.class);
//        verify(itemMapper).updateMainItemDto(captor.capture(), eq(orderItem));
//        assertEquals(mainItemDto, captor.getValue().build());
//    }
//
//    @Test
//    void getOrderCart() {
//        Order order = Order.builder().id(1000L).orderItems(List.of()).build();
//        when(orderService.getOrderWithItemsById(1L)).thenReturn(Optional.of(order));
//
//        cartService.getOrderCart(1L);
//
//        var captor = ArgumentCaptor.forClass(Order.class);
//        verify(orderItemMapper).toCartDto(captor.capture());
//        assertEquals(order.getId(), captor.getValue().getId());
//    }
}