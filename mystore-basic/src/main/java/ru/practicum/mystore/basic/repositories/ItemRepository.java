package ru.practicum.mystore.basic.repositories;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.practicum.mystore.basic.config.CacheConfig;
import ru.practicum.mystore.basic.data.entity.Item;

@Repository
public interface ItemRepository extends R2dbcRepository<Item, Long> {

    @Cacheable(CacheConfig.ITEMS_ORDER_NONE)
    Flux<Item> findAllByNameContaining(Pageable pageable, String nameFilter);

    @Cacheable(CacheConfig.ITEMS_ORDER_NAME)
    Flux<Item> findAllByNameContainingOrderByNameAsc(Pageable pageable, String nameFilter);

    @Cacheable(CacheConfig.ITEMS_ORDER_PRICE)
    Flux<Item> findAllByNameContainingOrderByPriceAsc(Pageable pageable, String nameFilter);

    @Cacheable(value = CacheConfig.ITEMS_BY_ID)
    Mono<Item> findById(Long itemId);
}
