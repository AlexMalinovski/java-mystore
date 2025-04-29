package ru.practicum.mystore.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.practicum.mystore.data.constant.SortType;
import ru.practicum.mystore.data.dto.MainItemDto;
import ru.practicum.mystore.data.dto.NewItemDto;
import ru.practicum.mystore.data.entity.Item;

import java.util.Optional;

public interface ItemService {
    Page<MainItemDto> findPaginated(Pageable pageable, String nameFilter, SortType sortType);

    Optional<Item> findById(long itemId);

    MainItemDto findOnly(long itemId);

    long addItem(NewItemDto item);
}
