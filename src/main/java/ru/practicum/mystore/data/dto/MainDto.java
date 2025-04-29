package ru.practicum.mystore.data.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.mystore.data.constant.SortType;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
public final class MainDto {
    private String nameSubstring;
    private SortType sort;
    private int pageSize;
    private int pageNumber;
}
