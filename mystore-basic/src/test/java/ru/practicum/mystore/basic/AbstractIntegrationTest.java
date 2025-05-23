package ru.practicum.mystore.basic;

import com.redis.testcontainers.RedisContainer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest
public abstract class AbstractIntegrationTest {

    private static RedisContainer redisContainer;

    static {
        redisContainer = new RedisContainer(DockerImageName.parse("redis:7.4.2-bookworm"))
                .withExposedPorts(6379);
        redisContainer.start();
    }

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.redis.host", redisContainer::getHost);
        registry.add("spring.data.redis.port", () -> redisContainer.getMappedPort(6379).toString());
    }
}
