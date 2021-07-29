package com.example.routingandfilteringgateway.services;

import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.stereotype.Service;

@Service
public class EncryptionAlgorithmService {
    public Algorithm getAlgorithm(String secret) {
        return Algorithm.HMAC256(secret);
    }
}
