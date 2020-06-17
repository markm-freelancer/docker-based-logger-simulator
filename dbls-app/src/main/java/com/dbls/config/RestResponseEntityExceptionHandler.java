package com.dbls.config;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.dbls.impl.exception.IllegalShiftIntervalException;
import com.dbls.impl.exception.IllegalTimingException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(IllegalShiftIntervalException.class)
    public ResponseEntity<GenericErrorResponse> handleIllegalShiftException(IllegalShiftIntervalException ex) {
        GenericErrorResponse error = new GenericErrorResponse(HttpStatus.BAD_REQUEST, ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(error);
    }

    @ExceptionHandler(IllegalTimingException.class)
    public ResponseEntity<GenericErrorResponse> handleIllegalTimingException(IllegalTimingException ex) {
        GenericErrorResponse error = new GenericErrorResponse(HttpStatus.BAD_REQUEST, ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(error);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<GenericErrorResponse> catchAll(Exception ex) {
        log.error("Unhandled exception", ex);
        GenericErrorResponse error = new GenericErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(error);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error("Invalid Method Argument {}", ex);
        GenericErrorResponse error = new GenericErrorResponse(HttpStatus.BAD_REQUEST, ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(error);
    }

    @Data
    public static class GenericErrorResponse {
        private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
        private HttpStatus status;
        private int statusCode;
        private String message;

        public GenericErrorResponse(HttpStatus status, Exception e) {
            this.status = status;
            statusCode = status.value();
            message = e.getMessage();
        }

        public void setMessage(String message) {
            this.message = message;
        }

        @Override
        public String toString() {
            try {
                return OBJECT_MAPPER.writeValueAsString(this);
            } catch (JsonProcessingException e) {
                return "{ \"message\": \"Unable to write exception as string\" }";
            }
        }
    }

}
