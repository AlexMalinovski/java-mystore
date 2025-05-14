package ru.practicum.mystore.basic.data.dto;

import lombok.Builder;
import lombok.Getter;
import ru.practicum.mystore.basic.data.constant.OrderStatus;

import java.util.List;

@Builder(toBuilder = true)
@Getter
public class OrderDto {
    private final List<String> itemsSummary;
    private final String costView;
    private final OrderStatus status;
    private final Long id;
    private final Long cost;
}
