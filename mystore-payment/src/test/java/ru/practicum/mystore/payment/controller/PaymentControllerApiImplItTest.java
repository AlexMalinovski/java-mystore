package ru.practicum.mystore.payment.controller;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import ru.practicum.mystore.payment.data.dto.PaymentDto;
import ru.practicum.mystore.payment.data.dto.PaymentRequest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class PaymentControllerApiImplItTest extends AbstractControllerTest {

    @Test
    void createPayment() {
        PaymentRequest paymentRequest = new PaymentRequest();
        when(paymentService.createPayment(any())).thenReturn(Mono.just(new PaymentDto()));
        webTestClient.post()
                .uri("/payment")
                .body(Mono.just(paymentRequest), PaymentRequest.class)
                .exchange()
                .expectStatus().isOk();

    }
}