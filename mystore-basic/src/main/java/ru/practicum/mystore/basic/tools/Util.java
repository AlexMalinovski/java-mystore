package ru.practicum.mystore.basic.tools;

import lombok.experimental.UtilityClass;
import reactor.core.publisher.Mono;
import ru.practicum.mystore.common.payment.data.dto.PaymentDto;

import java.math.BigDecimal;

@UtilityClass
public class Util {
    public String toPrice(long centsPrice) {
        final long dollars = centsPrice / 100;
        final long cents = centsPrice % 100;
        return String.format("%d.%d", dollars, cents);
    }

    public static Long toPriceInternal(BigDecimal price) {
        if (price == null) {
            return null;
        }
        long ceil = price.longValue();
        long digits = price.subtract(new BigDecimal(ceil)).multiply(new BigDecimal(100)).longValue();

        return 100 * ceil + digits;
    }

    public static Mono<PaymentDto> getFailPaymentDto(String message) {
        var result = new PaymentDto();
        result.setMessage(message);
        result.setStatus(PaymentDto.StatusEnum.ERROR);
        return Mono.just(result);
    }
}
