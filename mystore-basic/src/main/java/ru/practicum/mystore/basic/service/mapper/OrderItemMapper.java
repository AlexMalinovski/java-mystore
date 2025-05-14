package ru.practicum.mystore.basic.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.practicum.mystore.basic.data.dto.CartDto;
import ru.practicum.mystore.basic.data.entity.Item;
import ru.practicum.mystore.basic.data.entity.Order;
import ru.practicum.mystore.basic.data.entity.OrderItem;
import ru.practicum.mystore.basic.tools.Util;

import java.util.List;

@Mapper(config = DefaultMapperConfig.class, uses = {ItemMapper.class})
public interface OrderItemMapper {

    @Named("calculateOrderCost")
    default String calculateOrderCost(List<OrderItem> orderItems) {
        if (orderItems == null) {
            return null;
        }
        long sum = orderItems.stream()
                .mapToLong(item -> item.getItemQty() * item.getItemPrice())
                .sum();

        return Util.toPrice(sum);
    }

    @Mapping(target = "orderId", source = "order.id")
    @Mapping(target = "itemId", source = "item.id")
    @Mapping(target = "item", source = "item")
    @Mapping(target = "itemQty", constant = "0")
    @Mapping(target = "itemPrice", source = "item.price")
    @Mapping(target = "id", ignore = true)
    OrderItem toOrderItem(Order order, Item item);

    @Mapping(target = "items", source = "orderItems")
    @Mapping(target = "cost", source = "orderItems", qualifiedByName = "calculateOrderCost")
    CartDto toCartDto(Order src);
}
