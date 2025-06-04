package ru.practicum.mystore.payment.controller;

import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;
import reactor.core.publisher.Mono;
import ru.practicum.mystore.payment.data.dto.BalanceDto;

import static org.mockito.Mockito.when;

class BalanceControllerApiImplItTest extends AbstractControllerTest {

    @Test
    @WithMockUser(username = "user")
    void getBalance() {
        when(balanceService.getBalance()).thenReturn(Mono.just(new BalanceDto()));
        webTestClient.get()
                .uri("/balance")
                .exchange()
                .expectStatus().isOk();
    }
}