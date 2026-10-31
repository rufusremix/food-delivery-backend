package com.rufus.store.carts;

import lombok.Data;

@Data
public class CartRestaurantDto {
    private Long id;
    private String name;
    private String imageUrl;
    private Boolean isOpen;
}
