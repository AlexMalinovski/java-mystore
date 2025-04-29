package ru.practicum.mystore.controller;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import ru.practicum.mystore.data.dto.CartAction;
import ru.practicum.mystore.data.dto.CartDto;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CartControllerItTest extends AbstractWebMvcTest {

    @Test
    @SneakyThrows
    void cartAction_whenCartActionSourceIsNull_redirectToMain() {
        CartAction cartAction = new CartAction();
        when(cartService.handleCartAction(cartAction, 1L, null)).thenReturn(111L);
        mockMvc.perform(post(StoreUrls.Cart.ItemId.FULL.replaceAll("\\{itemId}", "1"))
                        .flashAttr("cartAction", cartAction))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(StoreUrls.Main.FULL));
    }

    @Test
    @SneakyThrows
    void cartAction_whenCartActionSource_redirectToSource() {
        CartAction cartAction = new CartAction();
        cartAction.setSource("/source");
        when(cartService.handleCartAction(cartAction, 1L, null)).thenReturn(111L);
        mockMvc.perform(post(StoreUrls.Cart.ItemId.FULL.replaceAll("\\{itemId}", "1"))
                        .flashAttr("cartAction", cartAction))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(cartAction.getSource()));
    }

    @SneakyThrows
    @Test
    void getCart() {
        when(cartService.getOrderCart(any())).thenReturn(CartDto.builder().items(List.of()).build());
        mockMvc.perform(get(StoreUrls.Cart.FULL))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(model().attributeExists("cart"));
    }
}