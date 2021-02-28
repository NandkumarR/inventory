package com.ikea.exception;

/**
 * @author nandk
 * Generic Exception object used for any validation/process failures.
 */
public class InventoryProcessException extends Exception {
    public InventoryProcessException(String errorMessage){
        super(errorMessage);
    }
}
