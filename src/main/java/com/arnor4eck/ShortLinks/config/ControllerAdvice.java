package com.arnor4eck.ShortLinks.config;

import com.arnor4eck.ShortLinks.security.handlers.ExceptionResponse;
import com.arnor4eck.ShortLinks.utils.exceptions.ExceptionResponseFactory;
import com.arnor4eck.ShortLinks.utils.exceptions.UserNotFoundException;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.hibernate.JDBCException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@AllArgsConstructor
@RestControllerAdvice
public class ControllerAdvice {

    private final ExceptionResponseFactory exceptionResponseFactory;

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ExceptionResponse methodArgumentNotValidException(MethodArgumentNotValidException e,
                                           HttpServletResponse response){

        List<String> errors = e.getBindingResult().getFieldErrors()
                            .stream()
                            .map(field ->
                                    field.getField() + ": " + field.getDefaultMessage())
                            .toList();

        return exceptionResponseFactory.create(HttpStatus.BAD_REQUEST,
                errors, response);
    }

    @ExceptionHandler({JDBCException.class})
    public ExceptionResponse jdbcException(JDBCException e, HttpServletResponse response){
        return exceptionResponseFactory.create(HttpStatus.BAD_REQUEST,
                e.getErrorMessage(), response);
    }

    @ExceptionHandler({RequestNotPermitted.class})
    public ExceptionResponse requestNotPermitted(RequestNotPermitted e, HttpServletResponse response){
        response.setHeader("Retry-After", "3");

        return exceptionResponseFactory.create(HttpStatus.TOO_MANY_REQUESTS,
                "Слишком много запросов. Повторите попытку",
                response);
    }

    @ExceptionHandler({UserNotFoundException.class})
    public ExceptionResponse notFoundException(UserNotFoundException e, HttpServletResponse response){
        return exceptionResponseFactory.create(HttpStatus.NOT_FOUND,
                e.getMessage(), response);
    }

    @ExceptionHandler({ResponseStatusException.class})
    public ExceptionResponse responseStatusException(ResponseStatusException e, HttpServletResponse response){
        return exceptionResponseFactory.create(HttpStatus.valueOf(e.getStatusCode().value()),
                e.getReason(), response);
    }
}
