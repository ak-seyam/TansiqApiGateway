package io.asiam.gateway.services;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service("devEnvService")
@RequiredArgsConstructor
public class DevEnvironmentService implements EnvironmentService{
    private final DotenvService dotenvService;
    @Override
    public String getEnv(String varName) {
        return dotenvService.getDotEnv().get(varName);
    }
}
