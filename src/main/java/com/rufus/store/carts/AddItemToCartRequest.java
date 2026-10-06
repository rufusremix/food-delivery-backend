package com.rufus.store.carts;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddItemToCartRequest {
    @NotNull(message = "Please provide productId")
    private Long productId;

    @Min(value = 1, message = "quantity must be at least one.")
    @Max(value = 1000, message = "quantity must not exceed 1000.")
    private Integer quantity = 1;
}
