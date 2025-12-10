package com.arnor4eck.ShortLinks;

import com.arnor4eck.ShortLinks.entity.short_url.ShortUrl;
import com.arnor4eck.ShortLinks.entity.user.role.Role;
import com.arnor4eck.ShortLinks.entity.user.User;
import com.arnor4eck.ShortLinks.repository.ShortUrlRepository;
import com.arnor4eck.ShortLinks.repository.UserRepository;
import com.arnor4eck.ShortLinks.utils.HashGenerator;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
@AllArgsConstructor
@Profile("dev")
public class DatabaseInitializer implements CommandLineRunner {
    private UserRepository userRepository;

    private ShortUrlRepository shortUrlRepository;

    private PasswordEncoder encoder;

    @Override
    public void run(String... args) throws Exception {
        userRepository.saveAll(
                List.of(
                        User.builder().username("arnor4eck").email("arnor4eck@gmail.com").role(Role.ADMIN).password(encoder.encode("password")).build(),
                        User.builder().username("user2").email("user2@mail.ru").role(Role.USER).password(encoder.encode("password")).build()
                )
        );

        User author = userRepository.findById(1L).get();

        HashGenerator hashGenerator = new HashGenerator();
        String originalUrl = "https://ru.wikipedia.org/wiki/%D0%A1%D1%82%D1%80%D0%B0%D0%BD%D1%8B-%D1%85%D0%BE%D0%B7%D1%8F%D0%B9%D0%BA%D0%B8_%D1%87%D0%B5%D0%BC%D0%BF%D0%B8%D0%BE%D0%BD%D0%B0%D1%82%D0%BE%D0%B2_%D0%BC%D0%B8%D1%80%D0%B0_%D0%BF%D0%BE_%D1%84%D1%83%D1%82%D0%B1%D0%BE%D0%BB%D1%83";


        shortUrlRepository.save(
                ShortUrl.builder().originalUrl(originalUrl)
                        .expiresAt(null)
                        .shortCode(hashGenerator.hash(originalUrl))
                        .author(author)
                        .build());
    }
}
