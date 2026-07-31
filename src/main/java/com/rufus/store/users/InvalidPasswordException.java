package com.rufus.store.users;

import com.rufus.store.common.BaseApiException;
import org.springframework.http.HttpStatus;

public class InvalidPasswordException extends BaseApiException {
    public InvalidPasswordException() {
        super(
                "Password does not match.",
                HttpStatus.FORBIDDEN,
                "/errors/invalid-password",
                "Invalid Password"
        );
    }
}
