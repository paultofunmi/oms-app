package com.assessment.oms.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.ArrayList;
import java.util.List;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException{

    private List<String> errors = new ArrayList<>();

    public NotFoundException(String message)  {
        super(message);
    }

    public NotFoundException(List<String> errors, String message)  {
        super(message);
        this.errors = errors;
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
}
