package com.arnor4eck.ShortLinks.security.cookie;

import com.arnor4eck.ShortLinks.entity.user.role.Role;
import com.arnor4eck.ShortLinks.entity.user.User;
import lombok.AllArgsConstructor;
import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Component
@AllArgsConstructor
public class CookieUtils {

    private static final String ALGORYTHM = "HmacSHA256";

    public static final String COOKIE_NAME = "auth_cookie";

    private final String DELIMITER = "&";

    private final int HMAC_LENGTH = 32; // длина в байтах

    private final CookieProperties cookieProperties;

    public byte[] generateHMAC(String string) throws NoSuchAlgorithmException, InvalidKeyException {
        Mac hmacSHA256 = Mac.getInstance(ALGORYTHM);

        SecretKeySpec secretKeySpec = new SecretKeySpec(
                cookieProperties.getSecret().getBytes(StandardCharsets.UTF_8), ALGORYTHM);
        hmacSHA256.init(secretKeySpec);

        return hmacSHA256.doFinal(string.getBytes(StandardCharsets.UTF_8));
    }

    public Cookie generateCookie(String token){
        Cookie cookie = new Cookie(CookieUtils.COOKIE_NAME, token);

        cookie.setHttpOnly(cookieProperties.isHttpOnly());
        cookie.setSecure(cookieProperties.isSecure());
        cookie.setPath(cookieProperties.getPath());
        cookie.setMaxAge((int) cookieProperties.getAge());

        return cookie;
    }

    private byte[] concat(byte[] arr1, byte[] arr2){
        byte[] combined = new byte[arr1.length + arr2.length];

        System.arraycopy(arr1, 0, combined, 0, arr1.length);
        System.arraycopy(arr2, 0, combined, arr1.length, arr2.length);

        return combined;
    }

    public String generateToken(User user) throws NoSuchAlgorithmException, InvalidKeyException {
        String content = String.join(DELIMITER, user.getRole().name(),
                        user.getEmail(), String.valueOf(System.currentTimeMillis()));

        byte[] toEncode = concat(content.getBytes(StandardCharsets.UTF_8), generateHMAC(content));

        return Base64.getEncoder().withoutPadding().encodeToString(toEncode);
    }

    private byte[] decodeToken(String token){
        return Base64.getDecoder().decode(token);
    }

    public boolean validateToken(String token) throws NoSuchAlgorithmException, InvalidKeyException {
        byte[] decoded = decodeToken(token);

        if (decoded.length < HMAC_LENGTH) {
            return false;
        }

        int contentLength = decoded.length - HMAC_LENGTH;
        byte[] content = new byte[contentLength];
        byte[] hmac = new byte[HMAC_LENGTH];

        System.arraycopy(decoded, 0, content, 0, contentLength); // [0; contentLength)
        System.arraycopy(decoded, contentLength, hmac, 0, HMAC_LENGTH); // [contentLength...decoded.length)

        String contentString = new String(content, StandardCharsets.UTF_8);
        byte[] generatedFromContent = generateHMAC(contentString);

        return checkTheHmac(generatedFromContent, hmac);
    }

    private boolean checkTheHmac(byte[] hmac1, byte[] hmac2){
        if(hmac1.length != hmac2.length)
            return false;

        byte ans = 0;
        for(int i = 0; i < hmac1.length; ++i)
            ans |= (byte) (hmac1[i] ^ hmac2[i]); // xor одинаковых битов дает 0

        return ans == 0; // значит дизъюнкция всех xor-ов равно 0
    }

    public Map<String, String> getContent(String token) throws NoSuchAlgorithmException, InvalidKeyException {
        if(!validateToken(token))
            throw new IllegalArgumentException("Некорректный токен");

        Map<String, String> content = new HashMap<>();

        byte[] decoded = decodeToken(token);
        byte[] contentBytes = new byte[decoded.length - HMAC_LENGTH];

        System.arraycopy(decoded, 0, contentBytes, 0, decoded.length - HMAC_LENGTH);

        String contentString = new String(contentBytes, StandardCharsets.UTF_8);

        String[] elements = contentString.split(DELIMITER);
        if(elements.length != 3)
            throw new IllegalArgumentException("Некорректный токен");

        content.put("role", elements[0]);
        content.put("email", elements[1]);

        return content;
    }

    public String getEmail(String token) throws NoSuchAlgorithmException, InvalidKeyException {
        return getContent(token).get("email");
    }

    public Role getRole(String token) throws NoSuchAlgorithmException, InvalidKeyException {
        return Role.valueOf(getContent(token).get("role"));
    }
}
