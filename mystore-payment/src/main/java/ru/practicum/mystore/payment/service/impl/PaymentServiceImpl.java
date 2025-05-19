package ru.practicum.mystore.payment.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.practicum.mystore.payment.data.dto.BalanceDto;
import ru.practicum.mystore.payment.data.dto.PaymentDto;
import ru.practicum.mystore.payment.data.dto.PaymentRequest;
import ru.practicum.mystore.payment.service.BalanceService;
import ru.practicum.mystore.payment.service.PaymentService;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final BalanceService balanceService;
    @Override
    public Mono<PaymentDto> createPayment(Mono<PaymentRequest> paymentRequest) {
        return balanceService.getBalance()
                .zipWith(paymentRequest)
                .map(t -> pay(t.getT1(), t.getT2()));
    }

    private PaymentDto pay(BalanceDto balanceDto,  PaymentRequest paymentRequest) {
        PaymentDto result = new PaymentDto();
        log.info("Проводим оплату");
        if (paymentRequest.getValue().compareTo(balanceDto.getValue()) > 0) {
            result.setStatus(PaymentDto.StatusEnum.ERROR);
            result.setMessage("Недостаточно средств");
            return result;
        }

        result.setStatus(PaymentDto.StatusEnum.SUCCESS);
        result.setMessage("Успешная оплата");
        return result;
    }
}
