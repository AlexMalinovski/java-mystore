package ru.practicum.mystore.payment.service.impl;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.practicum.mystore.payment.data.dto.BalanceDto;
import ru.practicum.mystore.payment.service.BalanceService;

import java.math.BigDecimal;

@Service
public class BalanceServiceImpl implements BalanceService {
    private static final BigDecimal DEFAULT_BALANCE = BigDecimal.valueOf(100);

    @Override
    public Mono<BalanceDto> getBalance() {
        return Mono.just(new BalanceDto())
                .doOnSuccess(dto -> dto.setValue(DEFAULT_BALANCE));
    }
}
