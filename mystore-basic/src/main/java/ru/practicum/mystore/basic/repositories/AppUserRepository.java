package ru.practicum.mystore.basic.repositories;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import ru.practicum.mystore.basic.data.entity.AppUser;

@Repository
public interface AppUserRepository extends R2dbcRepository<AppUser, Long> {
    Mono<AppUser> findOneByLogin(String username);
}
