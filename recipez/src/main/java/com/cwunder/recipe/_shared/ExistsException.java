package com.cwunder.recipe._shared;

public class ExistsException extends RuntimeException {
    public ExistsException(String entityName) {
        super(entityName);
    }
}
