package com.assessment.oms.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.ArrayList;
import java.util.List;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException{

    private List<String> errors = new ArrayList<>();

    public BadRequestException(String message)  {
        super(message);
    }

    public BadRequestException(List<String> errors, String message)  {
        super(message);
        this.errors = errors;
    }

    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
}
