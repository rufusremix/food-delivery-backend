package com.rufus.store.orders;

import com.rufus.store.users.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @EntityGraph(attributePaths = "items.product")
    @Query(value = "SELECT o FROM Order o WHERE o.customer = :customer",
           countQuery = "SELECT COUNT(o) FROM Order o WHERE o.customer = :customer")
    Page<Order> getOrdersByCustomer(@Param("customer") User customer, Pageable pageable);

    @EntityGraph(attributePaths = "items.product")
    @Query("SELECT o FROM Order o WHERE o.id = :orderId")
    Optional<Order> getOrderWithItems(@Param("orderId") Long orderId);
}
