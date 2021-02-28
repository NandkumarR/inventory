package com.ikea.exception;

import com.ikea.model.entity.ErrorResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author nandk
 * Controller Advicer that maps error response to JSON Response object ErrorResponse
 */
@RestControllerAdvice
@Order(value = Ordered.HIGHEST_PRECEDENCE)
public class InventoryExceptionAdvice {

    @ExceptionHandler({InventoryProcessException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleException(InventoryProcessException ex) {
        return new ErrorResponse(ex.getMessage());
    }
}
