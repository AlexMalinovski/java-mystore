package ru.practicum.mystore.basic.controller;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;
import ru.practicum.mystore.basic.data.dto.MainDto;
import ru.practicum.mystore.basic.data.dto.MainItemDto;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class MainControllerItTest extends AbstractControllerTest {

    @Test
    @SneakyThrows
    void setItemFilter() {
        var dto = new MainDto();
        MultipartBodyBuilder multipartBodyBuilder = new MultipartBodyBuilder();
        multipartBodyBuilder.part("mainDto", objectMapper.writeValueAsBytes(dto))
                .contentType(MediaType.MULTIPART_FORM_DATA);

        webTestClient.post()
                .uri(StoreUrls.Main.FULL)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(multipartBodyBuilder.build()))
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueEquals("Location", StoreUrls.Main.FULL);
    }

    @Test
    @SneakyThrows
    void getItems() {
        Page<MainItemDto> mainItems = Mockito.mock(Page.class);
        when(itemService.findPaginated(any(), any(), any())).thenReturn(Mono.just(mainItems));

        webTestClient.get()
                .uri(StoreUrls.Main.FULL)
                .exchange()
                .expectStatus().isOk();
    }
}