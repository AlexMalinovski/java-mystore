package ru.practicum.mystore.basic.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.practicum.mystore.basic.AbstractIntegrationTest;
import ru.practicum.mystore.basic.repositories.ItemRepository;
import ru.practicum.mystore.basic.repositories.OrderItemRepository;
import ru.practicum.mystore.basic.repositories.OrderRepository;

@Testcontainers
public class AbstractCacheTest extends AbstractIntegrationTest {
    @MockitoSpyBean
    protected OrderRepository orderRepository;

    @MockitoSpyBean
    protected ItemRepository itemRepository;

    @MockitoSpyBean
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
