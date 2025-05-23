package ru.practicum.mystore.payment.service;

import reactor.core.publisher.Mono;
import ru.practicum.mystore.payment.data.dto.PaymentDto;
import ru.practicum.mystore.payment.data.dto.PaymentRequest;

public interface PaymentService {
    Mono<PaymentDto> createPayment(Mono<PaymentRequest> paymentRequest);
}
