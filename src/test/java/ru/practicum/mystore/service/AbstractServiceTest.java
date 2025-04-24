package ru.practicum.mystore.service;

import org.springframework.beans.factory.annotation.Autowired;
import ru.practicum.mystore.AbstractIntegrationTest;
import ru.practicum.mystore.repositories.ItemRepository;
import ru.practicum.mystore.repositories.OrderItemRepository;
import ru.practicum.mystore.repositories.OrderRepository;

public class AbstractServiceTest extends AbstractIntegrationTest {
    @Autowired
    protected OrderRepository orderRepository;

    @Autowired
    protected ItemRepository itemRepository;

    @Autowired
    protected OrderItemRepository orderItemRepository;

    @Autowired
    protected OrderService orderService;

    @Autowired
    protected ItemService itemService;

    @Autowired
    protected OrderItemService orderItemService;

    @Autowired
    protected CartService cartService;
}
