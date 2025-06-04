package ru.practicum.mystore.basic.controller;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.practicum.mystore.basic.data.constant.OrderStatus;
import ru.practicum.mystore.basic.data.dto.CartDto;
import ru.practicum.mystore.basic.data.dto.OrderDto;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;

class OrderControllerItTest extends AbstractControllerTest {

    @Test
    @SneakyThrows
    @WithMockUser(username = "user")
    void placeOrder() {
        webTestClient.mutateWith(csrf()).post()
                .uri(StoreUrls.Orders.FULL)
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueEquals("Location", StoreUrls.Main.FULL);
    }

    @Test
    @SneakyThrows
    @WithMockUser(username = "user")
    void getOrderById() {
        when(cartService.getOrderCart(any())).thenReturn(Mono.just(CartDto.builder().status(OrderStatus.NEW).build()));
        webTestClient.get()
                .uri(StoreUrls.Orders.OrderId.FULL.replaceAll("\\{orderId}", "1"))
                .exchange()
                .expectStatus().isOk();

        verify(cartService).getOrderCart(1L);
    }

    @Test
    @SneakyThrows
    @WithMockUser(username = "user")
    void getOrders() {
        when(orderService.getAllOrders()).thenReturn(Flux.just(OrderDto.builder().build()));
        webTestClient.get()
                .uri(StoreUrls.Orders.FULL)
                .exchange()
                .expectStatus().isOk();

        verify(orderService).getAllOrders();
    }
}