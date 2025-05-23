package ru.practicum.mystore.basic.service;

import org.springframework.beans.factory.annotation.Autowired;
import ru.practicum.mystore.basic.AbstractIntegrationTest;
import ru.practicum.mystore.basic.repositories.ItemRepository;
import ru.practicum.mystore.basic.repositories.OrderItemRepository;
import ru.practicum.mystore.basic.repositories.OrderRepository;

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
