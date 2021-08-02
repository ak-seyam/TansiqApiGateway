package io.asiam.gateway.services;


import org.springframework.stereotype.Service;

@Service("prodEnvService")
public class ProdEnvironmentService implements EnvironmentService{
    @Override
    public String getEnv(String varName) {
        return System.getenv(varName);
    }
}
