package ru.practicum.mystore.basic;

import com.redis.testcontainers.RedisContainer;
import dasniko.testcontainers.keycloak.KeycloakContainer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest
public abstract class AbstractIntegrationTest {

    private static RedisContainer redisContainer;
    private static KeycloakContainer keycloakContainer;

    static {
        redisContainer = new RedisContainer(DockerImageName.parse("redis:7.4.2-bookworm"))
                .withExposedPorts(6379);
        redisContainer.start();

        keycloakContainer = new KeycloakContainer("quay.io/keycloak/keycloak:26.1.3");
        keycloakContainer.start();
    }

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.redis.host", redisContainer::getHost);
        registry.add("spring.data.redis.port", () -> redisContainer.getMappedPort(6379).toString());

        final String issuerUri = String.format("http://%s:%s/realms/master",
                keycloakContainer.getHost(), keycloakContainer.getMappedPort(8080).toString());
        registry.add("spring.security.oauth2.client.provider.keycloak.issuer-uri", () -> issuerUri);
    }
}
