package ru.practicum.mystore.basic;

import lombok.experimental.UtilityClass;
import ru.practicum.mystore.basic.data.constant.OrderStatus;
import ru.practicum.mystore.basic.data.dto.NewItemDto;
import ru.practicum.mystore.basic.data.entity.Item;
import ru.practicum.mystore.basic.data.entity.Order;
import ru.practicum.mystore.basic.data.entity.OrderItem;

import java.math.BigDecimal;
import java.util.List;

@UtilityClass
public class Util {
    public static final long ITEM_A_PRICE = 1500L;
    public static final int ITEM_A_QTY = 1;
    public static final String ITEM_A_NAME = "Item a";
    public static final String ITEM_A_DESCRIPTION = "Description a";

    public Item aItem() {
        return Item.builder()
                .price(ITEM_A_PRICE)
                .name(ITEM_A_NAME)
                .description(ITEM_A_DESCRIPTION)
                .build();
    }

    public NewItemDto aItemDto() {
        long price = ITEM_A_PRICE / 100;
        return NewItemDto.builder()
                .price(new BigDecimal(price))
                .name(ITEM_A_NAME)
                .description(ITEM_A_DESCRIPTION)
                .build();
    }

    public List<OrderItem> aOrderItems() {
        return List.of(
                OrderItem.builder()
                        .item(aItem())
                        .itemQty(ITEM_A_QTY)
                        .itemPrice(ITEM_A_PRICE)
                        .build()
        );
    }

    public Order aOrder() {
        return Order.builder()
                .orderItems(aOrderItems())
                .status(OrderStatus.NEW)
                .build();
    }
}
