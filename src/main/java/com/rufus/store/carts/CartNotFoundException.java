package com.rufus.store.carts;

import com.rufus.store.common.BaseApiException;
import org.springframework.http.HttpStatus;

public class CartNotFoundException extends BaseApiException {
    public CartNotFoundException() {
        super(
                "Cart not found",
                HttpStatus.NOT_FOUND,
                "/errors/cart-not-found",
                "Cart Not Found"
        );
    }
}
