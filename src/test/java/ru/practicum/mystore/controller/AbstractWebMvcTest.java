package ru.practicum.mystore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import ru.practicum.mystore.service.CartService;
import ru.practicum.mystore.service.ItemService;
import ru.practicum.mystore.service.OrderService;

@WebMvcTest(controllers = {
        CartController.class,
        ItemController.class,
        MainController.class,
        OrderController.class})
public abstract class AbstractWebMvcTest {
    @Autowired
    protected MockMvc mockMvc;

    protected ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    protected ItemService itemService;

    @MockitoBean
    protected CartService cartService;

    @MockitoBean
    protected OrderService orderService;
}
