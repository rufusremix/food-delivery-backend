package com.rufus.store.orders;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateDeliveryStatusRequest {
    @NotNull(message = "Delivery status is required")
    private DeliveryStatus deliveryStatus;
}
