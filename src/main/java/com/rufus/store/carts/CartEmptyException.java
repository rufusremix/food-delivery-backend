package com.rufus.store.carts;

import com.rufus.store.common.BaseApiException;
import org.springframework.http.HttpStatus;

public class CartEmptyException extends BaseApiException {
    public CartEmptyException() {
        super(
                "Cart is empty",
                HttpStatus.BAD_REQUEST,
                "/errors/cart-empty",
                "Cart Is Empty"
        );
    }
}