package ru.practicum.mystore.basic.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientProviderBuilder;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.logout.ServerLogoutHandler;
import org.springframework.web.server.WebSession;
import ru.practicum.mystore.basic.repositories.AppUserRepository;

import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AppUserRepository appUserRepository;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http

                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/main").permitAll()
                        .pathMatchers("/items/add").hasRole("USER")
                        .pathMatchers("/items/**").permitAll()
                        .anyExchange().hasRole("USER")
                )
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .formLogin(withDefaults())
                .logout(cust -> cust
                        .logoutHandler(logoutHandler()))
                .build();
    }

    @Bean
    ServerLogoutHandler logoutHandler() {
        return (exchange, auth) ->
                exchange.getExchange().getSession().flatMap(WebSession::invalidate);
    }

    @Bean
    public ReactiveAuthenticationManager reactiveAuthenticationManager() {
        UserDetailsRepositoryReactiveAuthenticationManager manager =
                new UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService());

        manager.setPasswordEncoder(passwordEncoder()); // пароль не используется
        return manager;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    ReactiveUserDetailsService userDetailsService() {
        PasswordEncoder encoder = passwordEncoder();
        return username ->
                appUserRepository.findOneByLogin(username)
                        .map(appUser -> new User(appUser.getLogin(), appUser.getPassword(), List.of(
                                new SimpleGrantedAuthority("ROLE_USER"))));
    }

    @Bean
    ReactiveOAuth2AuthorizedClientManager auth2AuthorizedClientManager(
            ReactiveClientRegistrationRepository clientRegistrationRepository,
            ReactiveOAuth2AuthorizedClientService authorizedClientService) {

        AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager manager =
                new AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager(clientRegistrationRepository, authorizedClientService);
        manager.setAuthorizedClientProvider(ReactiveOAuth2AuthorizedClientProviderBuilder.builder()
                .clientCredentials()
                .refreshToken()
                .build()
        );

        return manager;
    }
}
