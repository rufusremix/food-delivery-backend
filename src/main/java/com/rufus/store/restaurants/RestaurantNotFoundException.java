package com.rufus.store.restaurants;

import com.rufus.store.common.BaseApiException;
import org.springframework.http.HttpStatus;

public class RestaurantNotFoundException extends BaseApiException {
    public RestaurantNotFoundException() {
        super(
                "Restaurant not found",
                HttpStatus.NOT_FOUND,
                "/errors/restaurant-not-found",
                "Restaurant Not Found"
        );
    }
}
