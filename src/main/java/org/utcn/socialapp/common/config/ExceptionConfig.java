package org.utcn.socialapp.common.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.utcn.socialapp.common.exception.BusinessException;

import java.util.LinkedHashMap;
import java.util.Map;


@ControllerAdvice
public class ExceptionConfig {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity parseBusinessException(BusinessException ex) {
        return ResponseEntity
                .status(ex.getHttpStatus())
                .body(ex.getMessage());
    }
}
