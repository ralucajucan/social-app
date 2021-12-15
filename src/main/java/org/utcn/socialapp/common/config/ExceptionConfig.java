package org.utcn.socialapp.common.config;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.utcn.socialapp.common.exception.BusinessException;


@ControllerAdvice
public class ExceptionConfig {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Object> handleException(BusinessException e) {
        return ResponseEntity.status(e.getHttpStatus())
                             .body(e.getMessage());
    }
}
