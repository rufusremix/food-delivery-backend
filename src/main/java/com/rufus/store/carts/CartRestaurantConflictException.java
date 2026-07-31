package com.rufus.store.carts;

import com.rufus.store.common.BaseApiException;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

@Getter
public class CartRestaurantConflictException extends BaseApiException {
    private final String currentRestaurant;
    private final String attemptedRestaurant;

    public CartRestaurantConflictException() {
        this("Restaurant conflict with items.", null, null);
    }

    public CartRestaurantConflictException(String message, String currentRestaurant, String attemptedRestaurant) {
        super(
                message,
                HttpStatus.CONFLICT,
                "/errors/cart-conflict",
                "Restaurant Conflict in Cart"
        );
        this.currentRestaurant = currentRestaurant;
        this.attemptedRestaurant = attemptedRestaurant;
    }

    @Override
    public void customizeResponse(ProblemDetail problem) {
        if (currentRestaurant != null) {
            problem.setProperty("currentRestaurant", currentRestaurant);
        }
        if (attemptedRestaurant != null) {
            problem.setProperty("attemptedRestaurant", attemptedRestaurant);
        }
    }
}
