package ru.practicum.mystore.service.impl;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import ru.practicum.mystore.Util;
import ru.practicum.mystore.data.constant.SortType;
import ru.practicum.mystore.data.dto.MainItemDto;
import ru.practicum.mystore.data.entity.Item;
import ru.practicum.mystore.service.AbstractServiceTest;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ItemServiceImplItTest extends AbstractServiceTest {

    @Test
    void findPaginated() {
        var mono = itemService.findPaginated(PageRequest.of(0, 2), "", SortType.NO);

        mono.subscribe(actual -> {
            assertEquals(2, actual.getContent().size());
            assertEquals(3, actual.getTotalElements());
            assertEquals(2, actual.getTotalPages());
            Map<Long, MainItemDto> collect = actual.getContent().stream()
                    .collect(Collectors.toMap(MainItemDto::getId, Function.identity()));
            assertEquals("item2", collect.get(1L).getName());
            assertEquals("item1", collect.get(2L).getName());
        });
    }

    @Test
    void findById() {
        var mono = itemService.findById(1L);

        mono.subscribe(actual -> {
            assertNotNull(actual);
            assertEquals("item2", actual.getName());
        });

    }

    @Test
    void findOnly() {
        var mono = itemService.findOnly(1L);

        mono.subscribe(actual -> {
            assertEquals("item2", actual.getName());
        });
    }

    @Test
    void addItem() {
        Item expected = Util.aItem();

        itemService.addItem(Util.aItemDto())
                .flatMap(id -> itemRepository.findById(id))
                .subscribe(created -> {
                    assertNotNull(created.getId());
                    assertEquals(expected.getName(), created.getName());
                    assertEquals(expected.getDescription(), created.getDescription());
                    assertEquals(expected.getPrice(), created.getPrice());
                });
    }
}