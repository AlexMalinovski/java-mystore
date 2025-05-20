package ru.practicum.mystore.basic.data.dto;

import lombok.Builder;
import lombok.Getter;
import ru.practicum.mystore.basic.data.constant.OrderStatus;

import java.util.List;

@Builder(toBuilder = true)
@Getter
public class CartDto {
    private final List<MainItemDto> items;
    private String cost;
    private OrderStatus status;
    private Long id;
    private Boolean isAvailablePayment;
    private String systemMessage;
}
