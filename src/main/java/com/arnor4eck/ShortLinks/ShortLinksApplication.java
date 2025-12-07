package com.arnor4eck.ShortLinks;

import lombok.AllArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.arnor4eck.ShortLinks.DatabaseInitializer;

@SpringBootApplication
@AllArgsConstructor
public class ShortLinksApplication  {

	private DatabaseInitializer databaseInitializer;

	public static void main(String[] args) {
		SpringApplication.run(ShortLinksApplication.class, args);
	}

}
