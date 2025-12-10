package com.arnor4eck.ShortLinks;

import com.arnor4eck.ShortLinks.utils.HashGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.junit.jupiter.MockitoExtension;

import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class HashGeneratorTest {
    HashGenerator generator = new HashGenerator();

    HashGeneratorTest() throws NoSuchAlgorithmException {}

    @Test
    public void testHashGeneratorLength(){
        String[] args = new String[]{"lines", "with", "different", "length", "a", "bb"};
        int[] hashLengths = new int[args.length];
        for(int i = 0; i < args.length; ++i)
            hashLengths[i] = generator.hash(args[i]).length();

        assertArrayEquals(new int[]{22, 22, 22, 22, 22, 22}, hashLengths, "Длина всех хешей должна быть 22");
    }

    @Test
    public void testHashGeneratorAssert(){
        String hash = generator.hash("Hello, world!");

        assertEquals("31db788a4e641fc85bc7ed", hash);
    }
}
