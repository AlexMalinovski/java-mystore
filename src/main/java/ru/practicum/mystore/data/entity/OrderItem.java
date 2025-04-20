package ru.practicum.mystore.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.mystore.data.entity.id.OrderItemId;

import java.util.Objects;

@Entity
@Table(name = "orders_items")
@IdClass(OrderItemId.class)
@Builder(toBuilder = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem {

    @Id
    @Column(name = "order_id", nullable = false)
    private Long orderId;

    @Id
    @Column(name = "item_id", nullable = false)
    private Long itemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("itemId")
    @JoinColumn(name = "item_id")
    private Item item;

    @Column(name = "item_qty", nullable = false)
    private Integer itemQty;

    @Column(name = "item_price", nullable = false)
    private Long itemPrice;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        OrderItem orderItem = (OrderItem) o;
        if (!Objects.equals(orderId, orderItem.orderId)) {
            return false;
        }
        return Objects.equals(itemId, orderItem.itemId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
