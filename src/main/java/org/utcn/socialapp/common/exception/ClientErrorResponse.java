package org.utcn.socialapp.common.exception;

import org.springframework.http.HttpStatus;

public enum ClientErrorResponse {
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "Bad Request!"),
    BAD_CREDENTIALS(HttpStatus.BAD_REQUEST, "Bad User Credentials!"),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "Unauthorized!"),
    DISABLED(HttpStatus.UNAUTHORIZED, "User Is Disabled!"),
    LOCKED(HttpStatus.UNAUTHORIZED, "User Is Locked!"),
    PAYMENT_REQUIRED(HttpStatus.PAYMENT_REQUIRED, "Payment Required!"),
    FORBIDDEN(HttpStatus.FORBIDDEN, "Forbidden Action!"),
    EXPIRED_TOKEN(HttpStatus.FORBIDDEN, "Token is expired!"),
    EXPIRED_SESSION(HttpStatus.FORBIDDEN,"Session has expired!"),
    NOT_FOUND(HttpStatus.NOT_FOUND, "Not Found!"),
    NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "Action Not Allowed!"),
    CONFLICT(HttpStatus.CONFLICT, "Conflict with the server state!"),
    CONFLICT_REGISTER(HttpStatus.CONFLICT, "User Credentials Already Taken!"),
    CONFLICT_TOKEN(HttpStatus.CONFLICT, "User is already enabled.");
    public final HttpStatus status;
    public final String message;

    ClientErrorResponse(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
