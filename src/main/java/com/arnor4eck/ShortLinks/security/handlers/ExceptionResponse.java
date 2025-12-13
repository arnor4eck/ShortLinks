package com.arnor4eck.ShortLinks.security.handlers;

import java.util.List;

public record ExceptionResponse(int code, List<String> messages) {
    public ExceptionResponse(int code, String message){
        this(code, List.of(message));
    }
}
