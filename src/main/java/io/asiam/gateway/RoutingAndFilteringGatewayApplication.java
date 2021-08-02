package io.asiam.gateway;

import io.asiam.gateway.models.Admin;
import io.asiam.gateway.repositories.AdminsRepo;
import io.asiam.gateway.services.EnvironmentService;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@EnableZuulProxy
@EnableEurekaClient
public class RoutingAndFilteringGatewayApplication {

    @Autowired
    private AdminsRepo adminsRepo;
    @Autowired
    @Qualifier("devEnvService")
    private EnvironmentService environmentService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public static void main(String[] args) {
        SpringApplication.run(RoutingAndFilteringGatewayApplication.class, args);
    }

    @Bean
    public CommandLineRunner runner() {
        return (args) -> {
            if (environmentService.getEnv("BASE_ADMIN_EMAIL") != null && environmentService.getEnv("BASE_ADMIN_PASSWORD") != null) {
                try {
                    adminsRepo.save(new Admin(
                            environmentService.getEnv("BASE_ADMIN_EMAIL"),
                            passwordEncoder.encode(environmentService.getEnv("BASE_ADMIN_PASSWORD"))
                    ));
                } catch (Exception e) {
                    System.out.println("Error: "+e.getMessage());
                }
            }
        };
    }

}
