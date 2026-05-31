package com.rufus.store.payments;


import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class CheckoutRequest {
    @NotNull(message = "Cart ID is required.")
    private UUID cartId;

    @NotNull(message = "Delivery address ID is required.")
    private Long addressId;
}
