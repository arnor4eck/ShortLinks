package com.arnor4eck.ShortLinks.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ua_parser.Parser;


@Configuration
public class UaParserConfig {
    @Bean
    public Parser uaParser() {
        return new Parser();
    }
}
