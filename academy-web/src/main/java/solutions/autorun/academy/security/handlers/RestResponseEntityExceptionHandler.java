package solutions.autorun.academy.security.handlers;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import solutions.autorun.academy.exceptions.FileManagerException;
import solutions.autorun.academy.security.exceptions.UserNotActivatedException;


import java.io.IOException;
import java.nio.file.AccessDeniedException;

@ControllerAdvice
public class RestResponseEntityExceptionHandler
        extends ResponseEntityExceptionHandler {

    @ExceptionHandler({ AccessDeniedException.class })
    public ResponseEntity<Object> handleAccessDeniedException(
            Exception ex, WebRequest request) {
        return new ResponseEntity<>(
                "Access denied", new HttpHeaders(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({ AuthenticationException.class })
    public ResponseEntity<Object> authenticationException(
            Exception ex, WebRequest request) {
        return new ResponseEntity<>(
                ex.getMessage(), new HttpHeaders(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({ FileManagerException.class })
    public ResponseEntity<Object> fileManagerException(
            Exception ex, WebRequest request) {
        return new ResponseEntity<>(
                ex.getMessage(), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({ IOException.class })
    public ResponseEntity<Object> IOException(
            Exception ex, WebRequest request) {
        return new ResponseEntity<>(
                ex.getMessage(), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({ UserNotActivatedException.class })
    public ResponseEntity<Object> UserNotActivatedException(
            Exception ex, WebRequest request) {
        return new ResponseEntity<>(
                "Account is not activated", new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
