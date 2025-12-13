package com.arnor4eck.ShortLinks.utils;

import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Component
public final class HashGenerator {
    private final MessageDigest digest;

    public HashGenerator(String algorythm) throws NoSuchAlgorithmException {
        digest = MessageDigest.getInstance(algorythm);
    }

    public HashGenerator() throws NoSuchAlgorithmException {
        this("SHA-256");
    }

    public String hash(String inputLine){
        StringBuilder hexString = new StringBuilder();
        byte[] hashBytes = digest.digest(inputLine.getBytes(StandardCharsets.UTF_8));

        for(int i = 0; i < hashBytes.length; ++i){
            if(i % 3 == 0) {
                String curHex = Integer.toHexString(0xff & hashBytes[i]); // получение неотрицательного значения
                if (curHex.length() == 1)
                    hexString.append('0'); // добавление ведущего нуля, чтобы длина строки была четной
                hexString.append(curHex);
            }
        }

        return hexString.toString();
    }
}
