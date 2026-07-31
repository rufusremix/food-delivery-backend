package com.rufus.store.users;

import com.rufus.store.common.BaseApiException;
import org.springframework.http.HttpStatus;

public class DuplicateUserException extends BaseApiException {
    public DuplicateUserException() {
        super(
                "Email is already registered.",
                HttpStatus.CONFLICT,
                "/errors/duplicate-user",
                "Duplicate User"
        );
    }
}
