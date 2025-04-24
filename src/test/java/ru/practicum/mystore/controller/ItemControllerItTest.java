package ru.practicum.mystore.controller;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import ru.practicum.mystore.data.dto.MainItemDto;
import ru.practicum.mystore.data.dto.NewItemDto;

import java.math.BigDecimal;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ItemControllerItTest extends AbstractWebMvcTest {

    @Test
    @SneakyThrows
    void getItem() {
        when(itemService.findOnly(1L)).thenReturn(MainItemDto.builder().build());
        mockMvc.perform(get(StoreUrls.Items.ItemId.FULL.replaceAll("\\{itemId}", "1")))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(model().attributeExists("item"));
    }

    @Test
    @SneakyThrows
    void getItemEditor() {
        mockMvc.perform(get(StoreUrls.Items.Add.FULL))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(model().attributeExists("newItemDto"));
    }

    @Test
    @SneakyThrows
    void addItem() {
        NewItemDto dto = NewItemDto.builder().name("name").description("descr").price(new BigDecimal(100)).build();
        when(itemService.addItem(dto)).thenReturn(1L);
        mockMvc.perform(post(StoreUrls.Items.Add.FULL)
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                        .flashAttr("newItemDto", dto))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(
                        StoreUrls.Items.ItemId.FULL.replaceAll("\\{itemId}", String.valueOf(1L))));
    }
}