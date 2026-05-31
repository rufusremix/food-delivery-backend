package com.rufus.store.restaurants;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "restaurants")
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "cuisine")
    private String cuisine;

    @Column(name = "address")
    private String address;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "is_open")
    private Boolean isOpen;

    @Column(name = "delivery_fee")
    private BigDecimal deliveryFee;

    @PrePersist
    void applyDefaults() {
        if (isOpen == null) {
            isOpen = true;
        }
        if (deliveryFee == null) {
            deliveryFee = BigDecimal.ZERO;
        }
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "name = " + name + ", " +
                "cuisine = " + cuisine + ")";
    }
}
