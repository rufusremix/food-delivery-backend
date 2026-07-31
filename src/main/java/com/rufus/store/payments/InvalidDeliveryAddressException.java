package com.rufus.store.payments;

import com.rufus.store.common.BaseApiException;
import org.springframework.http.HttpStatus;

public class InvalidDeliveryAddressException extends BaseApiException {
    public InvalidDeliveryAddressException() {
        super(
                "Invalid delivery address.",
                HttpStatus.FORBIDDEN,
                "/errors/invalid-delivery-address",
                "Invalid Delivery Address"
        );
    }
}
