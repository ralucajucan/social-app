package org.utcn.socialapp.common.exception;

import org.springframework.http.HttpStatus;

public class BusinessException extends Exception {
    private HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;


    public BusinessException(ClientErrorResponse clientErrorResponse) {
        super(clientErrorResponse.message);
        this.httpStatus = clientErrorResponse.status;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }
}
