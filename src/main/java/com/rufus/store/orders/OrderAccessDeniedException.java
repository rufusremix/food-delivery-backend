package com.rufus.store.orders;

import com.rufus.store.common.BaseApiException;
import org.springframework.http.HttpStatus;

public class OrderAccessDeniedException extends BaseApiException {
    public OrderAccessDeniedException() {
        super(
                "You don't have access to this order.",
                HttpStatus.FORBIDDEN,
                "/errors/order-access-denied",
                "Order Access Denied"
        );
    }
}
