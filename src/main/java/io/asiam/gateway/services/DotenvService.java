package io.asiam.gateway.services;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DotenvService {
    @Bean
    Dotenv getDotEnv() {
        return Dotenv.load();
    }
}
