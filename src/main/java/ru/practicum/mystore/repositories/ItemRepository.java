package ru.practicum.mystore.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import ru.practicum.mystore.data.entity.Item;

@Repository
public interface ItemRepository extends R2dbcRepository<Item, Long> {
    Flux<Item> findAllByNameContaining(Pageable pageable, String nameFilter);

    Flux<Item> findAllByNameContainingOrderByNameAsc(Pageable pageable, String nameFilter);

    Flux<Item> findAllByNameContainingOrderByPriceAsc(Pageable pageable, String nameFilter);
}
