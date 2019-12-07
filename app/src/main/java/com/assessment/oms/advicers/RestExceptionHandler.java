package com.assessment.oms.advicers;


import com.assessment.oms.exception.BadRequestException;
import com.assessment.oms.exception.NotFoundException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class RestExceptionHandler {

    JSONObject response;

    RestExceptionHandler(){
        response = new JSONObject();
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ResponseEntity badRequestHandler(BadRequestException ex) {
        response.put("success", false);
        response.put("payload", new JSONArray());
        response.put("errors", ex.getErrors());
        response.put("message", ex.getMessage());
        return new ResponseEntity(response.toMap(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    ResponseEntity notFoundHandler(NotFoundException ex) {
        response.put("success", false);
        response.put("payload", new JSONArray());
        response.put("errors", ex.getErrors());
        response.put("message", ex.getMessage());
        return new ResponseEntity(response.toMap(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ResponseEntity exceptionHandler(Exception ex) {
        response.put("success", false);
        response.put("payload", new JSONArray());
        response.put("errors", new JSONArray());
        response.put("message", ex.getMessage());
        return new ResponseEntity(response.toMap(), HttpStatus.BAD_REQUEST);
    }
}

