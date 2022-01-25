package com.broadcom.challenge.web.error;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.NativeWebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ValidationException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller advice to translate the server side exceptions to client-friendly json structures.
 */
@ControllerAdvice
public class ExceptionTranslator {


    @ExceptionHandler({BadRequestAlertException.class})
    public ResponseEntity<Error> handleBadRequestAlertException(BadRequestAlertException ex, NativeWebRequest request) {
        Error error = new Error(ex.getMessage(),ex.getErrors(), ex.getErrorKey(), ex.getEntityName());
        return new ResponseEntity<Error>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ValidationException.class})
    public ResponseEntity<Error> handleBadRequestAlertException(ValidationException ex, NativeWebRequest request) {
        Error error = new Error(ex.getMessage(),null, "400", "User");
        return new ResponseEntity<Error>(error, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler
    public ResponseEntity<Error> handleBadRequestAlertException(Exception ex, NativeWebRequest request) {
        Error error = new Error(ex.getMessage(),null, "500", "User");
        return new ResponseEntity<Error>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    static class Error{
        private String message;
        private String errorCode;
        private String entity;
        private List<String> errors;
        public Error(String message, List<String> errors, String errorCode, String entity){
            this.errorCode  = errorCode;
            this.message= message;
            this.entity = entity;
            this.errors = errors;
        }
        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getErrorCode() {
            return errorCode;
        }

        public void setErrorCode(String errorCode) {
            this.errorCode = errorCode;
        }

        public String getEntity() {
            return entity;
        }

        public void setEntity(String entity) {
            this.entity = entity;
        }
    }
}
