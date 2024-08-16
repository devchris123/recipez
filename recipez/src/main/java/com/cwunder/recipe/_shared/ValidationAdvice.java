package com.cwunder.recipe._shared;

import org.springframework.hateoas.mediatype.problem.Problem;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ValidationAdvice {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Problem> handleException(MethodArgumentNotValidException ex) {
        return ProblemBuilder.buildProblem(ex.getBindingResult());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Problem> handleException(IllegalArgumentException ex) {
        return ProblemBuilder.buildProblem(ex.getMessage() != null ? ex.getMessage() : "Argument error");
    }

    @ExceptionHandler(CaptchaErrorException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Problem> handleException(CaptchaErrorException ex) {
        return ProblemBuilder.buildProblem(ex.getMessage() != null ? ex.getMessage() : "Argument error",
                ex.getErrorCodeMap());
    }
}
