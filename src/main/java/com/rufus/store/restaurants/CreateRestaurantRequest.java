package com.rufus.store.restaurants;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateRestaurantRequest {
    @NotBlank(message = "Name is required")
    @Size(max = 255, message = "Name must be less than 255 characters")
    private String name;

    @Size(max = 500, message = "Description must be less than 500 characters")
    private String description;

    @Size(max = 200, message = "Cuisine must be less than 200 characters")
    private String cuisine;

    @Size(max = 255, message = "Address must be less than 255 characters")
    private String address;

    @Size(max = 500, message = "Image URL must be less than 500 characters")
    private String imageUrl;

    private Boolean isOpen = true;

    @PositiveOrZero(message = "Delivery fee must be zero or positive")
    private BigDecimal deliveryFee = BigDecimal.ZERO;
}
