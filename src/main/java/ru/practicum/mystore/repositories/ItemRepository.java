package ru.practicum.mystore.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.mystore.data.entity.Item;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    Page<Item> findAllByNameContaining(Pageable pageable, String nameFilter);

    Page<Item> findAllByNameContainingOrderByNameAsc(Pageable pageable, String nameFilter);

    Page<Item> findAllByNameContainingOrderByPriceAsc(Pageable pageable, String nameFilter);
}
