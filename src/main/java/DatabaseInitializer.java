import com.arnor4eck.ShortLinks.entity.user.Role;
import com.arnor4eck.ShortLinks.entity.user.User;
import com.arnor4eck.ShortLinks.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class DatabaseInitializer implements CommandLineRunner {
    private UserRepository userRepository;

    private PasswordEncoder encoder;

    @Override
    public void run(String... args) throws Exception {
        userRepository.saveAll(
                List.of(
                        User.builder().username("arnor4eck").email("arnor4eck@gmail.com").role(Role.ADMIN).password(encoder.encode("password")).build(),
                        User.builder().username("user2").email("user2@mail.ru").role(Role.USER).password(encoder.encode("password")).build()
                )
        );
    }
}
