package com.rufus.store.auth;

import com.rufus.store.common.BaseApiException;
import org.springframework.http.HttpStatus;

public class InvalidRefreshTokenException extends BaseApiException {
    public InvalidRefreshTokenException() {
        super(
                "Invalid or expired refresh token.",
                HttpStatus.UNAUTHORIZED,
                "/errors/invalid-refresh-token",
                "Unauthorized"
        );
    }
}
