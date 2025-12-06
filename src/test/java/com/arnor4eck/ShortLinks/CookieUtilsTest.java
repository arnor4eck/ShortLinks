package com.arnor4eck.ShortLinks;

import com.arnor4eck.ShortLinks.entity.user.Role;
import com.arnor4eck.ShortLinks.entity.user.User;
import com.arnor4eck.ShortLinks.security.cookie.CookieProperties;
import com.arnor4eck.ShortLinks.security.cookie.CookieUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@ExtendWith(MockitoExtension.class)
public class CookieUtilsTest {

    @Autowired
    private CookieUtils cookieUtils;

    @BeforeEach
    public void setUp(){
        cookieUtils = new CookieUtils(new CookieProperties());
    }

    @Test
    public void testGenerateTokenTrue() throws NoSuchAlgorithmException, InvalidKeyException {
        String token = cookieUtils.generateToken(new User(1, "arnor4eck", "arnor4eck@mail.ru",
                "password", Role.ADMIN));

        Assertions.assertTrue(cookieUtils.validateToken(token));
    }

    @Test
    public void testEmailFromToken() throws NoSuchAlgorithmException, InvalidKeyException {
        String userEmail = "arnor4eck@mail.ru";
        User mockUser = new User(1, "arnor4eck", userEmail,
                "password", Role.ADMIN);

        String token = cookieUtils.generateToken(mockUser);

        Assertions.assertEquals(userEmail, cookieUtils.getEmail(token));
    }

    @Test
    public void testRoleFromToken() throws NoSuchAlgorithmException, InvalidKeyException {
        Role userRole = Role.ADMIN;
        User mockUser = new User(1, "arnor4eck", "arnor4eck@mail.ru",
                "password", userRole);

        String token = cookieUtils.generateToken(mockUser);

        Assertions.assertEquals(userRole, cookieUtils.getRole(token));
    }
}
