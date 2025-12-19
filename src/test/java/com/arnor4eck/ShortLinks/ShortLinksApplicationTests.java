package com.arnor4eck.ShortLinks;

import com.arnor4eck.ShortLinks.config.TestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@Import({TestConfig.class})
class ShortLinksApplicationTests {

	@Test
	void contextLoads() {
	}

}
