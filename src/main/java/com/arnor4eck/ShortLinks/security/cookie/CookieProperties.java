package com.arnor4eck.ShortLinks.security.cookie;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@NoArgsConstructor
@ConfigurationProperties(prefix = "application.cookie")
public class CookieProperties {
    long age = 1000L;
    String secret = "secret-cookie";
}
