package ru.practicum.mystore.basic.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app-pref")
@Getter
@Setter
@ToString
public class AppPref {
    private ServiceConnection paymentService;
}
