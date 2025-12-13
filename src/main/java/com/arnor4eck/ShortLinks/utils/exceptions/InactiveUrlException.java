package com.arnor4eck.ShortLinks.utils.exceptions;

public class InactiveUrlException extends RuntimeException {
    public InactiveUrlException(String message) {
        super(message);
    }
}
