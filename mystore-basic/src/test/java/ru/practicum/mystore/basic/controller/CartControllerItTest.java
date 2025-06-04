package ru.practicum.mystore.basic.controller;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;
import ru.practicum.mystore.basic.data.dto.CartAction;
import ru.practicum.mystore.basic.data.dto.CartDto;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;

class CartControllerItTest extends AbstractControllerTest {

    @Test
    @SneakyThrows
    @WithMockUser(username = "user")
    void cartAction_whenCartActionSourceIsNull_redirectToMain() {
        CartAction cartAction = new CartAction();
        when(cartService.handleCartAction(cartAction, 1L, null)).thenReturn(Mono.just(111L));
        webTestClient.mutateWith(csrf()).post()
                .uri(StoreUrls.Cart.ItemId.FULL.replaceAll("\\{itemId}", "1"))
                .body(BodyInserters.fromValue(cartAction))
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueEquals("Location", StoreUrls.Main.FULL);
    }

    @SneakyThrows
    @WithMockUser(username = "user")
    @Test
    void getCart() {
        when(cartService.getOrderCart(any())).thenReturn(Mono.just(CartDto.builder().items(List.of()).build()));
        webTestClient.get()
                .uri(StoreUrls.Cart.FULL)
                .exchange()
                .expectStatus().isOk();
    }
}