package com.arnor4eck.ShortLinks;

import com.arnor4eck.ShortLinks.entity.user.Role;
import com.arnor4eck.ShortLinks.entity.user.User;
import com.arnor4eck.ShortLinks.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.EnumSet;
import java.util.List;

@SpringBootApplication
@AllArgsConstructor
public class ShortLinksApplication  {

	public static void main(String[] args) {
		SpringApplication.run(ShortLinksApplication.class, args);
	}

}
