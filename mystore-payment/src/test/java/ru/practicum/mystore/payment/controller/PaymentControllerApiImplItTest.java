package ru.practicum.mystore.payment.controller;

import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;
import reactor.core.publisher.Mono;
import ru.practicum.mystore.payment.data.dto.PaymentDto;
import ru.practicum.mystore.payment.data.dto.PaymentRequest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;

class PaymentControllerApiImplItTest extends AbstractControllerTest {

    @Test
    @WithMockUser(username = "user")
    void createPayment() {
        PaymentRequest paymentRequest = new PaymentRequest();
        when(paymentService.createPayment(any())).thenReturn(Mono.just(new PaymentDto()));
        webTestClient.mutateWith(csrf()).post()
                .uri("/payment")
                .body(Mono.just(paymentRequest), PaymentRequest.class)
                .exchange()
                .expectStatus().isOk();

    }
}