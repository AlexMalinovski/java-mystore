package ru.practicum.mystore.repositories;

import jakarta.annotation.Nonnull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.mystore.data.entity.Order;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @EntityGraph("order-fetch-items")
    Optional<Order> findOrderById(Long id);

    @Nonnull
    @EntityGraph("order-fetch-items")
    List<Order> findAll();
}
