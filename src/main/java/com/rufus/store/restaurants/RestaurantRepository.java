package com.rufus.store.restaurants;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    @Query(
        value = "SELECT DISTINCT r FROM Restaurant r " +
                "LEFT JOIN Product p ON p.restaurant.id = r.id " +
                "WHERE LOWER(r.name) LIKE LOWER(CONCAT('%', :search, '%')) " +
                "OR LOWER(p.name) LIKE LOWER(CONCAT('%', :search, '%'))",
        countQuery = "SELECT COUNT(DISTINCT r) FROM Restaurant r " +
                     "LEFT JOIN Product p ON p.restaurant.id = r.id " +
                     "WHERE LOWER(r.name) LIKE LOWER(CONCAT('%', :search, '%')) " +
                     "OR LOWER(p.name) LIKE LOWER(CONCAT('%', :search, '%'))"
    )
    Page<Restaurant> findBySearchTerm(@Param("search") String search, Pageable pageable);
}
