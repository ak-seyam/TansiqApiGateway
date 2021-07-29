package com.example.routingandfilteringgateway;

import com.example.routingandfilteringgateway.models.Admin;
import com.example.routingandfilteringgateway.repositories.AdminsRepo;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.beans.factory.annotation.Autowired;
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
    private Dotenv dotenv;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public static void main(String[] args) {
        SpringApplication.run(RoutingAndFilteringGatewayApplication.class, args);
    }

    @Bean
    public CommandLineRunner runner() {
        return (args) -> {
            if (dotenv.get("BASE_ADMIN_EMAIL") != null && dotenv.get("BASE_ADMIN_PASSWORD") != null) {
                try {
                    adminsRepo.save(new Admin(
                            dotenv.get("BASE_ADMIN_EMAIL"),
                            passwordEncoder.encode(dotenv.get("BASE_ADMIN_PASSWORD"))
                    ));
                } catch (Exception e) {
                    System.out.println("Error: "+e.getMessage());
                }
            }
        };
    }

}
