package com.rufus.store.products;

import com.rufus.store.common.BaseApiException;
import org.springframework.http.HttpStatus;

public class ProductNotFoundException extends BaseApiException {
    public ProductNotFoundException() {
        super(
                "Product not found",
                HttpStatus.NOT_FOUND,
                "/errors/product-not-found",
                "Product Not Found"
        );
    }
}
