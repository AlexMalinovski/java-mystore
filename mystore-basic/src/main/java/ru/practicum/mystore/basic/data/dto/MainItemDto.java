package ru.practicum.mystore.basic.data.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder(toBuilder = true)
@EqualsAndHashCode
@Getter
public class MainItemDto {
    private final Long id;
    private final String name;
    private final String description;
    private final Long price;
    private final Integer cartCnt;
    private final String img;
    private final String priceView;
}
