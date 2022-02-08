package org.utcn.socialapp.common.exception;

import org.springframework.http.HttpStatus;

public class BusinessException extends Exception {
    private final HttpStatus httpStatus;


    public BusinessException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public BusinessException(ClientErrorResponse clientErrorResponse) {
        super(clientErrorResponse.message);
        this.httpStatus = clientErrorResponse.status;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
