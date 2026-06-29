package com.rufus.store.carts;

public class CartRestaurantConflictException extends RuntimeException {
    public CartRestaurantConflictException() {
        super("Restaurant conflict with items.");
    }
}
