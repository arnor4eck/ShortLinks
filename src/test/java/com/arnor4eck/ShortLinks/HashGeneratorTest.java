package com.arnor4eck.ShortLinks;

import com.arnor4eck.ShortLinks.utils.HashGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Description;

import java.security.NoSuchAlgorithmException;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class HashGeneratorTest {
    HashGenerator generator;

    @BeforeEach
    public void setUp() throws NoSuchAlgorithmException {
        generator = new HashGenerator();
    }

    public static Stream<String> argsMethod(){
        return Stream.of("line", "",
                "testtesttettses", "arnor4eck");
    }

    @ParameterizedTest
    @MethodSource("argsMethod")
    @Description("Длина всех хешей должна быть 22")
    public void testHashGeneratorLength(String arg){
        String hash = generator.hash(arg);
        assertEquals( 22, hash.length());
    }

    @Test
    public void testHashGeneratorAssert(){
        String hash = generator.hash("Hello, world!");
        assertEquals("31db788a4e641fc85bc7ed", hash);
    }
}
