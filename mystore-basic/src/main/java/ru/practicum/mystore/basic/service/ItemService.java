package ru.practicum.mystore.basic.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Mono;
import ru.practicum.mystore.basic.data.constant.SortType;
import ru.practicum.mystore.basic.data.dto.MainItemDto;
import ru.practicum.mystore.basic.data.dto.NewItemDto;
import ru.practicum.mystore.basic.data.entity.Item;

public interface ItemService {
    Mono<Page<MainItemDto>> findPaginated(Pageable pageable, String nameFilter, SortType sortType);

    Mono<Item> findById(long itemId);

    Mono<MainItemDto> findOnly(long itemId);

    Mono<Long> addItem(NewItemDto item);
}
