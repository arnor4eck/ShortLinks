package com.arnor4eck.ShortLinks.config;

import com.arnor4eck.ShortLinks.security.handlers.ExceptionResponse;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import jakarta.servlet.http.HttpServletResponse;
import org.hibernate.JDBCException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ExceptionResponse methodArgumentNotValidException(MethodArgumentNotValidException e,
                                           HttpServletResponse response){

        List<String> errors = e.getBindingResult().getFieldErrors()
                            .stream()
                            .map(field ->
                                    field.getField() + ": " + field.getDefaultMessage())
                            .toList();

        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

        return new ExceptionResponse(e.getStatusCode().value(), errors);
    }

    @ExceptionHandler({JDBCException.class})
    public ExceptionResponse jdbcException(JDBCException e, HttpServletResponse response){
        int errorCode = HttpStatus.BAD_REQUEST.value();

        response.setStatus(errorCode);
        return new ExceptionResponse(errorCode, e.getErrorMessage());
    }

    @ExceptionHandler({RequestNotPermitted.class})
    public ExceptionResponse requestNotPermitted(RequestNotPermitted e, HttpServletResponse response){
        int errorCode = HttpStatus.TOO_MANY_REQUESTS.value();

        response.setStatus(errorCode);
        response.setHeader("Retry-After", "3");

        return new ExceptionResponse(errorCode, "Слишком много запросов. Повторите попытку");
    }

    @ExceptionHandler({ResponseStatusException.class})
    public ExceptionResponse responseStatusException(ResponseStatusException e, HttpServletResponse response){
        response.setStatus(e.getStatusCode().value());
        return new ExceptionResponse(e.getStatusCode().value(), e.getReason());
    }
}
