package ru.practicum.mystore.payment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import ru.practicum.mystore.payment.service.BalanceService;
import ru.practicum.mystore.payment.service.PaymentService;


@WebFluxTest(controllers = {
        BalanceControllerApi.class,
        PaymentControllerApi.class})
public abstract class AbstractControllerTest {
    @Autowired
    protected WebTestClient webTestClient;

    protected ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    protected BalanceService balanceService;

    @MockitoBean
    protected PaymentService paymentService;
}
