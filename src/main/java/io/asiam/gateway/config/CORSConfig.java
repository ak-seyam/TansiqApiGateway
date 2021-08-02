package io.asiam.gateway.config;


import io.asiam.gateway.services.EnvironmentService;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CORSConfig {
    private final EnvironmentService environmentService;

    @Autowired
    public CORSConfig(@Qualifier("devEnvService") EnvironmentService environmentService) {
        this.environmentService = environmentService;
    }

    @Bean
    public WebMvcConfigurer corsConfig() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins(environmentService.getEnv("UI_ORIGIN"))
                        .allowedMethods("*")
                        .allowCredentials(true);
            }
        };
    }
}
