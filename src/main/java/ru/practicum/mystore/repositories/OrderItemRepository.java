package ru.practicum.mystore.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.mystore.data.entity.OrderItem;
import ru.practicum.mystore.data.entity.id.OrderItemId;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, OrderItemId> {
    List<OrderItem> findAllByOrderId(long orderId);
}
