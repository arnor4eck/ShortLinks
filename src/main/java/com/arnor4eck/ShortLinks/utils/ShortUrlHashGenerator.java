package com.arnor4eck.ShortLinks.utils;

import org.springframework.stereotype.Component;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;

@Component
public class ShortUrlHashGenerator {
    private final HashGenerator generator;

    public ShortUrlHashGenerator() throws NoSuchAlgorithmException {
        generator = new HashGenerator();
    }

    public String generate(String authorEmail, LocalDate createdAt, String url){
        String totalString = String.join("",
                authorEmail, createdAt.toString(), url);
        return generator.hash(totalString);
    }
}
