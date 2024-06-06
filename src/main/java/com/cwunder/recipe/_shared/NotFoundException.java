package com.cwunder.recipe._shared;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String entityName) {
        super(entityName);
    }
}
