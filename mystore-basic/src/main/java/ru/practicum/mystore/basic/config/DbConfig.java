package ru.practicum.mystore.basic.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.practicum.mystore.basic.data.constant.OrderStatus;
import ru.practicum.mystore.basic.data.entity.Item;
import ru.practicum.mystore.basic.data.entity.Order;
import ru.practicum.mystore.basic.data.entity.OrderItem;
import ru.practicum.mystore.basic.repositories.ItemRepository;
import ru.practicum.mystore.basic.repositories.OrderItemRepository;
import ru.practicum.mystore.basic.repositories.OrderRepository;

import java.util.List;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DbConfig {
    private final ItemRepository itemRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    @Bean
    ApplicationRunner initDb() {
        return args -> {
            orderItemRepository.deleteAll().block();
            orderRepository.deleteAll().block();
            itemRepository.deleteAll().block();

            Item item1 = Item.builder().name("item2").description("description2").price(300L).build();
            Item item2 = Item.builder().name("item1").description("description1").price(500L).build();
            Item item3 = Item.builder().name("item3").description("description3").price(100L).build();
            itemRepository.saveAll(List.of(item1, item2, item3)).blockLast();

            Order order1 = Order.builder().status(OrderStatus.NEW).build();
            orderRepository.saveAll(List.of(order1)).blockLast();

            OrderItem orderItem1 = OrderItem.builder()
                    .orderId(order1.getId())
                    .itemId(item1.getId())
                    .itemQty(3)
                    .itemPrice(item1.getPrice())
                    .build();
            OrderItem orderItem2 = OrderItem.builder()
                    .orderId(order1.getId())
                    .itemId(item2.getId())
                    .itemQty(1)
                    .itemPrice(item2.getPrice())
                    .build();
            orderItemRepository.saveAll(List.of(orderItem1, orderItem2)).blockLast();

            log.info("БД инициализирована демо-значениями");
        };
    }

}
