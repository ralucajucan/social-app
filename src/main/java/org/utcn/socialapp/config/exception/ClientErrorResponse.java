package org.utcn.socialapp.config.exception;

public enum ClientErrorResponse {
    BAD_REQUEST(400, "Bad Request!"),
    BAD_CREDENTIALS(400, "Bad User Credentials!"),
    UNAUTHORIZED(401, "Unauthorized!"),
    DISABLED(401, "User Is Disabled!"),
    LOCKED(401, "User Is Locked!"),
    PAYMENT_REQUIRED(402, "Payment Required!"),
    FORBIDDEN(403, "Forbidden Action!"),
    NOT_FOUND(404, "Not Found!"),
    NOT_ALLOWED(405, "Action Not Allowed!"),
    EXPIRED_TOKEN(405, "Token is expired!"),
    CONFLICT(409, "Conflict with the server state!"),
    CONFLICT_REGISTER(409, "User Credentials Already Taken!"),
    CONFLICT_TOKEN(409, "User is already enabled.");
    public final Integer status;
    public final String message;

    ClientErrorResponse(Integer status, String message) {
        this.status = status;
        this.message = message;
    }
}
