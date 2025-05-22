package ru.practicum.mystore.basic.service.impl;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import ru.practicum.mystore.basic.data.constant.SortType;
import ru.practicum.mystore.basic.data.dto.NewItemDto;
import ru.practicum.mystore.basic.service.AbstractCacheTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class ItemServiceImplCacheItTest extends AbstractCacheTest {

    @Test
    void findPaginated_whenNoSort() {
        PageRequest pageRequest = PageRequest.of(0, 2);
        for (int i = 0; i < 3; i++) {
            itemService.findPaginated(pageRequest, "", SortType.NO)
                    .subscribe(actual -> {
                        assertEquals(2, actual.getContent().size());
                        verify(itemRepository, times(1)).findAllByNameContaining(pageRequest, "");
                    });
        }
    }

    @Test
    @SneakyThrows
    void findPaginated_whenPriceSort() {
        PageRequest pageRequest = PageRequest.of(0, 2);
        for (int i = 0; i < 3; i++) {
            itemService.findPaginated(pageRequest, "", SortType.PRICE)
                    .subscribe(actual -> {
                        assertEquals(2, actual.getContent().size());
                        verify(itemRepository, times(1))
                                .findAllByNameContainingOrderByPriceAsc(pageRequest, "");
                    });
        }
    }

    @Test
    @SneakyThrows
    void findPaginated_whenAlphaSort() {
        PageRequest pageRequest = PageRequest.of(0, 2);
        for (int i = 0; i < 3; i++) {
            itemService.findPaginated(pageRequest, "", SortType.ALPHA)
                    .subscribe(actual -> {
                        assertEquals(2, actual.getContent().size());
                        verify(itemRepository, times(1))
                                .findAllByNameContainingOrderByNameAsc(pageRequest, "");
                    });
        }
    }

    @Test
    void findById() {
        for (int i = 0; i < 3; i++) {
            itemService.findById(1L).subscribe(actual -> {
                assertEquals(1L, actual.getId());
                verify(itemRepository, times(1)).findById(1L);
            });
        }
    }

    @Test
    void addItem() {
        PageRequest pageRequest = PageRequest.of(0, 2);
        itemService
                .findPaginated(pageRequest, "", SortType.ALPHA)
                .subscribe(found -> {
                    itemService.addItem(NewItemDto.builder()
                            .name("aaaname").description("descr").price(new BigDecimal(100)).build());
                    for (int i = 0; i < 3; i++) {
                        itemService.findPaginated(pageRequest, "", SortType.ALPHA)
                                .subscribe(actual -> {
                                    assertEquals(2, actual.getContent().size());
                                    verify(itemRepository, times(2))
                                            .findAllByNameContainingOrderByNameAsc(pageRequest, "");
                                });
                    }
                });
    }
}