package com.cwunder.recipe;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RecipeNotFoundAdvice {
    @ExceptionHandler(RecipeNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    NotFoundResponse recipeNotFoundHandler(RecipeNotFoundException ex) {
        return new NotFoundResponse(ex.getMessage());
    }

    record NotFoundResponse(String msg) {
    }
}
