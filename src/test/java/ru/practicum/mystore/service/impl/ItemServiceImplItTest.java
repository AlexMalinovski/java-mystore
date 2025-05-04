package ru.practicum.mystore.service.impl;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;
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
import static org.junit.jupiter.api.Assertions.assertTrue;

class ItemServiceImplItTest extends AbstractServiceTest {

//    @Test
//    void findPaginated() {
//        Page<MainItemDto> actual = itemService.findPaginated(PageRequest.of(0, 2), "", SortType.NO);
//
//        assertEquals(2, actual.getContent().size());
//        assertEquals(3, actual.getTotalElements());
//        assertEquals(2, actual.getTotalPages());
//        Map<Long, MainItemDto> collect = actual.getContent().stream()
//                .collect(Collectors.toMap(MainItemDto::getId, Function.identity()));
//        assertEquals("item2", collect.get(1L).getName());
//        assertEquals("item1", collect.get(2L).getName());
//    }
//
//    @Test
//    void findById() {
//        var actual = itemService.findById(1L);
//
//        assertTrue(actual.isPresent());
//        assertEquals("item2", actual.get().getName());
//    }
//
//    @Test
//    void findOnly() {
//        MainItemDto actual = itemService.findOnly(1L);
//
//        assertEquals("item2", actual.getName());
//    }
//
//    @Test
//    @Transactional
//    void addItem() {
//        Item expected = Util.aItem();
//        long itemId = itemService.addItem(Util.aItemDto());
//
//        var created = itemRepository.findById(itemId).get();
//        assertNotNull(created.getId());
//        assertEquals(expected.getName(), created.getName());
//        assertEquals(expected.getDescription(), created.getDescription());
//        assertEquals(expected.getPrice(), created.getPrice());
//    }
}