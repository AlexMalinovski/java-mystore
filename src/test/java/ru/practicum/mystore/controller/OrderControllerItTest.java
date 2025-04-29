package ru.practicum.mystore.controller;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import ru.practicum.mystore.data.constant.OrderStatus;
import ru.practicum.mystore.data.dto.CartDto;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

class OrderControllerItTest extends AbstractWebMvcTest {

    @Test
    @SneakyThrows
    void placeOrder() {
        mockMvc.perform(post(StoreUrls.Orders.FULL)
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                        .sessionAttr("orderId", 1L))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(
                        StoreUrls.Orders.OrderId.FULL.replaceAll(
                                "\\{orderId}", String.valueOf(1L)) + "?isNew=true"));
        verify(orderService).placeOrder(1L);
    }

    @Test
    @SneakyThrows
    void getOrderById() {
        when(cartService.getOrderCart(any())).thenReturn(CartDto.builder().status(OrderStatus.NEW).build());
        mockMvc.perform(get(StoreUrls.Orders.OrderId.FULL.replaceAll("\\{orderId}", "1")))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(model().attributeExists("cart"))
                .andExpect(view().name("order"));

        verify(cartService).getOrderCart(1L);
    }

    @Test
    @SneakyThrows
    void getOrders() {
        mockMvc.perform(get((StoreUrls.Orders.FULL)))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(model().attributeExists("orders"))
                .andExpect(view().name("orders"));

        verify(orderService).getAllOrders();
    }
}