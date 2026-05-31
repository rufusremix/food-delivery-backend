package com.rufus.store.restaurants;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class RestaurantDto {
    private Long id;
    private String name;
    private String description;
    private String cuisine;
    private String address;
    private String imageUrl;
    private Boolean isOpen;
    private BigDecimal deliveryFee;
}
