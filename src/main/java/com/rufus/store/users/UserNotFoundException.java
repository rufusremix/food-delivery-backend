package com.rufus.store.users;

import com.rufus.store.common.BaseApiException;
import org.springframework.http.HttpStatus;

public class UserNotFoundException extends BaseApiException {
    public UserNotFoundException() {
        super(
                "User not found",
                HttpStatus.NOT_FOUND,
                "/errors/user-not-found",
                "User Not Found"
        );
    }
}
