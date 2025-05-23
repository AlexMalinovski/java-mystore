package ru.practicum.mystore.basic.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import ru.practicum.mystore.common.payment.client.BalanceControllerApi;
import ru.practicum.mystore.common.payment.client.PaymentControllerApi;
import ru.practicum.mystore.common.payment.service.ApiClient;

@Configuration
@RequiredArgsConstructor
public class AppConfig {
    private final AppPref appPref;

    @Bean
    ApiClient paymentServiceApiClient() {
        var apiClient = new ApiClient(WebClient.create());
        apiClient.setBasePath(appPref.getPaymentService().getUrl());
        return apiClient;
    }

    @Bean
    BalanceControllerApi balanceControllerApiClient() {
        return new BalanceControllerApi(paymentServiceApiClient());
    }

    @Bean
    PaymentControllerApi paymentControllerApiClient() {
        return new PaymentControllerApi(paymentServiceApiClient());
    }
}
