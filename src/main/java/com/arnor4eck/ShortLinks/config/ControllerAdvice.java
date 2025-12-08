package com.arnor4eck.ShortLinks.config;

import com.arnor4eck.ShortLinks.security.handlers.ExceptionResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.hibernate.JDBCException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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
        return new ExceptionResponse(errorCode, List.of(e.getErrorMessage()));
    }
}
