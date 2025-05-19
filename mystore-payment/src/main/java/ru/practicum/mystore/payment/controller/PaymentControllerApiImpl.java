package ru.practicum.mystore.payment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import ru.practicum.mystore.payment.data.dto.PaymentDto;
import ru.practicum.mystore.payment.data.dto.PaymentRequest;
import ru.practicum.mystore.payment.service.PaymentService;

@RestController
@RequiredArgsConstructor
public class PaymentControllerApiImpl implements PaymentControllerApi {
    private final PaymentService paymentService;
    @Override
    public Mono<ResponseEntity<PaymentDto>> createPayment(
            Mono<PaymentRequest> paymentRequest, ServerWebExchange exchange) {
        return paymentService.createPayment(paymentRequest)
                .map(ResponseEntity.ok()::body);
    }
}
