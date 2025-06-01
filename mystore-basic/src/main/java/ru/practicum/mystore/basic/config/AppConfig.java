package ru.practicum.mystore.basic.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import ru.practicum.mystore.common.payment.client.BalanceControllerApi;
import ru.practicum.mystore.common.payment.client.PaymentControllerApi;
import ru.practicum.mystore.common.payment.service.ApiClient;

@Configuration
@RequiredArgsConstructor
public class AppConfig {
    private final AppPref appPref;
    private final ReactiveOAuth2AuthorizedClientManager manager;

    @Bean
    ApiClient paymentServiceApiClient() {

        ServerOAuth2AuthorizedClientExchangeFilterFunction oauth =
                new ServerOAuth2AuthorizedClientExchangeFilterFunction(manager);
        oauth.setDefaultClientRegistrationId("mystore");

        var apiClient = new ApiClient(
                WebClient.builder()
                        .filter(oauth)
                        .build());

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
