package ru.practicum.mystore.basic.data.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SortType {
    NO("нет"),
    ALPHA("по алфавиту"),
    PRICE("по цене");

    private final String description;
}
