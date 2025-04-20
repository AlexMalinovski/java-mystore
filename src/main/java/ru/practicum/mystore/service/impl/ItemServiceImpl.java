package ru.practicum.mystore.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.mystore.data.constant.SortType;
import ru.practicum.mystore.data.dto.MainItemDto;
import ru.practicum.mystore.data.dto.NewItemDto;
import ru.practicum.mystore.data.entity.Item;
import ru.practicum.mystore.exception.NotFoundException;
import ru.practicum.mystore.repositories.ItemRepository;
import ru.practicum.mystore.service.ItemService;
import ru.practicum.mystore.service.mapper.ItemMapper;

import java.util.Optional;
import java.util.function.BiFunction;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;

    private BiFunction<Pageable, String, Page<Item>> getFindMethodWithSort(SortType sortType) {
        return switch (sortType) {
            case ALPHA -> itemRepository::findAllByNameContainingOrderByNameAsc;
            case PRICE -> itemRepository::findAllByNameContainingOrderByPriceAsc;
            default -> itemRepository::findAllByNameContaining;
        };
    }

    @Override
    public Page<MainItemDto> findPaginated(Pageable pageable, String nameFilter, SortType sortType) {
        return getFindMethodWithSort(sortType).apply(pageable, nameFilter)
                .map(itemMapper::toMainItemDto);
    }

    @Override
    public Optional<Item> findById(long itemId) {
        return itemRepository.findById(itemId);
    }

    @Override
    public MainItemDto findOnly(long itemId) {
        return itemRepository.findById(itemId)
                .map(itemMapper::toMainItemDto)
                .orElseThrow(
                        () -> new NotFoundException(String.format("Товар с артикулом '%s' не найден", itemId)));
    }

    @Override
    public long addItem(NewItemDto itemDto) {
        Item item = itemMapper.toItem(itemDto);
        return itemRepository.save(item).getId();
    }
}
