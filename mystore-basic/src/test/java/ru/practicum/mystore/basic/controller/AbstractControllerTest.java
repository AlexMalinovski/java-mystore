package ru.practicum.mystore.basic.controller;

import dasniko.testcontainers.keycloak.KeycloakContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import ru.practicum.mystore.basic.service.CartService;
import ru.practicum.mystore.basic.service.ItemService;
import ru.practicum.mystore.basic.service.OrderService;

@WebFluxTest(controllers = {
        CartController.class,
        ItemController.class,
        MainController.class,
        OrderController.class})
@AutoConfigureWebTestClient
public abstract class AbstractControllerTest {
    private static KeycloakContainer keycloakContainer;

    @Autowired
    protected WebTestClient webTestClient;

    protected ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    protected ItemService itemService;

    @MockitoBean
    protected CartService cartService;

    @MockitoBean
    protected OrderService orderService;

    static {
        keycloakContainer = new KeycloakContainer("quay.io/keycloak/keycloak:26.1.3");
        keycloakContainer.start();
    }

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        final String issuerUri = String.format("http://%s:%s/realms/master",
                keycloakContainer.getHost(), keycloakContainer.getMappedPort(8080).toString());
        registry.add("spring.security.oauth2.client.provider.keycloak.issuer-uri", () -> issuerUri);
    }
}
