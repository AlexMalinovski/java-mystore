package ru.practicum.mystore.payment.service;

import reactor.core.publisher.Mono;
import ru.practicum.mystore.payment.data.dto.BalanceDto;

public interface BalanceService {
    Mono<BalanceDto> getBalance();
}
