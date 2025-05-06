package ru.practicum.mystore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import ru.practicum.mystore.service.CartService;
import ru.practicum.mystore.service.ItemService;
import ru.practicum.mystore.service.OrderService;

@WebFluxTest(controllers = {
        CartController.class,
        ItemController.class,
        MainController.class,
        OrderController.class})
public abstract class AbstractControllerTest {
    @Autowired
    protected WebTestClient webTestClient;

    protected ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    protected ItemService itemService;

    @MockitoBean
    protected CartService cartService;

    @MockitoBean
    protected OrderService orderService;
}
