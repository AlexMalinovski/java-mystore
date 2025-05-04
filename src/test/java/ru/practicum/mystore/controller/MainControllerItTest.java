package ru.practicum.mystore.controller;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import ru.practicum.mystore.data.dto.MainDto;
import ru.practicum.mystore.data.dto.MainItemDto;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MainControllerItTest extends AbstractWebMvcTest {

//    @Test
//    @SneakyThrows
//    void setItemFilter() {
//        mockMvc.perform(post(StoreUrls.Main.FULL)
//                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
//                        .content(objectMapper.writeValueAsBytes(new MainDto())))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl(StoreUrls.Main.FULL));
//    }
//
//    @Test
//    @SneakyThrows
//    void getItems() {
//        Page<MainItemDto> mainItems = Mockito.mock(Page.class);
//        when(itemService.findPaginated(any(), any(), any())).thenReturn(mainItems);
//        mockMvc.perform(get(StoreUrls.Main.FULL))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType("text/html;charset=UTF-8"))
//                .andExpect(model().attributeExists("itemsPage"));
//    }
}