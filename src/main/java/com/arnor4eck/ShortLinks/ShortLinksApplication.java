package com.arnor4eck.ShortLinks;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ShortLinksApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ShortLinksApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

	}
}
