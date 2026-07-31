package com.rufus.store.payments;

import com.rufus.store.common.BaseApiException;
import org.springframework.http.HttpStatus;

public class PaymentException extends BaseApiException {
    public PaymentException() {
        this("Payment processing failed.");
    }

    public PaymentException(String message) {
        super(
                message,
                HttpStatus.BAD_GATEWAY,
                "/errors/payment-failed",
                "Payment Failed"
        );
    }
}
