package com.arnor4eck.ShortLinks.utils.exceptions;

import com.arnor4eck.ShortLinks.security.handlers.ExceptionResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ExceptionResponseFactory {
    public ExceptionResponse create(HttpStatus status,
                                    List<String> errors, HttpServletResponse response){
        response.setStatus(status.value());
        return new ExceptionResponse(status.value(), errors);
    }

    public ExceptionResponse create(HttpStatus status,
                                    String error, HttpServletResponse response){
        return create(status, List.of(error), response);
    }
}
