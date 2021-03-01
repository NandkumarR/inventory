package com.ikea.exception;

import com.ikea.model.entity.ErrorResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartException;

/**
 * @author nandk
 * Controller Advicer that maps custom error execption object to JSON Response object ErrorResponse
 */
@RestControllerAdvice
@Order(value = Ordered.HIGHEST_PRECEDENCE)
public class InventoryExceptionAdvice {

    @ExceptionHandler(InventoryProcessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleCustomException(InventoryProcessException ex) {
        return new ErrorResponse(ex.getMessage());
    }

    @ExceptionHandler(MultipartException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleFileException(MultipartException ex) {
        return new ErrorResponse(ex.getMessage());
    }
}
