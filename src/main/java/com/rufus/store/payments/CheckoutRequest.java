package com.rufus.store.payments;


import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CheckoutRequest {
    @NotNull(message = "Delivery address ID is required.")
    private Long addressId;
}
