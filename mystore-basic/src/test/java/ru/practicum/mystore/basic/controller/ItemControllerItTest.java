package ru.practicum.mystore.basic.controller;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;
import ru.practicum.mystore.basic.data.dto.MainItemDto;
import ru.practicum.mystore.basic.data.dto.NewItemDto;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class ItemControllerItTest extends AbstractControllerTest {

    @Test
    @SneakyThrows
    void getItem() {
        when(itemService.findOnly(1L)).thenReturn(Mono.just(MainItemDto.builder().build()));
        webTestClient.get()
                .uri(StoreUrls.Items.ItemId.FULL.replaceAll("\\{itemId}", "1"))
                .exchange()
                .expectStatus().isOk();
    }

    //
    @Test
    @SneakyThrows
    void getItemEditor() {
        webTestClient.get()
                .uri(StoreUrls.Items.Add.FULL)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    @SneakyThrows
    void addItem() {
        NewItemDto dto = NewItemDto.builder().build();

        MultipartBodyBuilder multipartBodyBuilder = new MultipartBodyBuilder();
        multipartBodyBuilder.part("newItemDto", objectMapper.writeValueAsBytes(dto))
                .contentType(MediaType.MULTIPART_FORM_DATA);


        when(itemService.addItem(any())).thenReturn(Mono.just(1L));
        webTestClient.post()
                .uri(StoreUrls.Items.Add.FULL)
                .contentType(MediaType.MULTIPART_FORM_DATA)

                .body(BodyInserters.fromMultipartData(multipartBodyBuilder.build()))
                .exchange()
                .expectStatus().isOk();
    }
}