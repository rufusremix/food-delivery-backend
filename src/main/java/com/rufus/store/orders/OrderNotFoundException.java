package com.rufus.store.orders;

import com.rufus.store.common.BaseApiException;
import org.springframework.http.HttpStatus;

public class OrderNotFoundException extends BaseApiException {
    public OrderNotFoundException() {
        super(
                "Order not found",
                HttpStatus.NOT_FOUND,
                "/errors/order-not-found",
                "Order Not Found"
        );
    }
}
