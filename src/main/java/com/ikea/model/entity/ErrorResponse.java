package com.ikea.model.entity;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * @author nandk
 * Error Object shown in case of errors thrown.
 */
public class ErrorResponse implements Serializable{

    private List<String> errors;

    public ErrorResponse(String errors) {
        this.errors = Arrays.asList(errors);
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
}
