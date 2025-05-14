package ru.practicum.mystore.basic.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.practicum.mystore.basic.data.dto.OrderDto;
import ru.practicum.mystore.basic.data.entity.Order;
import ru.practicum.mystore.basic.data.entity.OrderItem;
import ru.practicum.mystore.basic.tools.Util;

import java.util.List;

@Mapper(config = DefaultMapperConfig.class)
public interface OrderMapper {
    default String toItemSummary(OrderItem orderItem) {
        if (orderItem == null) {
            return null;
        }
        final int qty = orderItem.getItemQty();
        final long amount = qty * orderItem.getItemPrice();
        return String.format("%s кол-во:%s стоимость:%s",
                orderItem.getItem().getName(), qty, Util.toPrice(amount));
    }

    @Named("toItemSummaryList")
    List<String> toItemSummary(List<OrderItem> src);

    @Named("toOrderCost")
    default String toOrderCost(List<OrderItem> src) {
        if (src == null) {
            return null;
        }
        final long total = src.stream()
                .filter(orderItem -> orderItem.getItemQty() > 0)
                .mapToLong(orderItem -> orderItem.getItemQty() * orderItem.getItemPrice())
                .sum();
        return Util.toPrice(total);
    }

    @Mapping(target = "itemsSummary", source = "src.orderItems", qualifiedByName = "toItemSummaryList")
    @Mapping(target = "costView", source = "src.orderItems", qualifiedByName = "toOrderCost")
    OrderDto toOrderDto(Order src);
}
