package ru.practicum.mystore.payment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import ru.practicum.mystore.payment.data.dto.BalanceDto;
import ru.practicum.mystore.payment.service.BalanceService;

@RestController
@RequiredArgsConstructor
public class BalanceControllerApiImpl implements BalanceControllerApi {

    private final BalanceService balanceService;

    @Override
    public Mono<ResponseEntity<BalanceDto>> getBalance(ServerWebExchange exchange) {
        return balanceService.getBalance()
                .map(ResponseEntity.ok()::body);
    }
}
