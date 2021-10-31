package org.utcn.socialapp.common.exception;

public class BusinessException extends Exception {
    public Integer status;

    public BusinessException(ClientErrorResponse clientErrorResponse) {
        super(clientErrorResponse.message);
        this.status = clientErrorResponse.status;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
