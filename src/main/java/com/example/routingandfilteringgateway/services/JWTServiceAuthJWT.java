package com.example.routingandfilteringgateway.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.routingandfilteringgateway.models.JWTPayloadData;
import io.github.cdimascio.dotenv.Dotenv;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class JWTServiceAuthJWT implements JWTService {
    private final Dotenv dotenv;
    private final EncryptionAlgorithmService algorithmService;

    @Autowired
    public JWTServiceAuthJWT(Dotenv dotenv, EncryptionAlgorithmService algorithmService) {
        this.dotenv = dotenv;
        this.algorithmService = algorithmService;
    }

    /**
     * @param payload
     * @param secret
     * @param expirationTime in seconds
     * @return
     */
    private String getJwt(JWTPayloadData payload, String secret, Integer expirationTime) {
        Algorithm algorithm = algorithmService.getAlgorithm(secret);
        return JWT.create()
                .withSubject(payload.getEmail())
                .withExpiresAt(new Date(System.currentTimeMillis() + (expirationTime * 1000)))
                .withClaim("role", payload.getRoles().get(0))
                .sign(algorithm);
    }

    @Override
    public String getAccessToken(JWTPayloadData payload) {
        // TODO make expiration 15 mins
        return getJwt(payload, dotenv.get("ACCESS_TOKEN_SECRET"), 15 * 60);
    }

    @Override
    public String getRefreshToken(JWTPayloadData payload) {
        return getJwt(payload, dotenv.get("REFRESH_TOKEN_SECRET"), 7 * 24 * 3600);
    }

    private JWTPayloadData verifyToken(String token, String secret) {
        try {
            Algorithm algorithm = algorithmService.getAlgorithm(secret);
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT decodedJWT = verifier.verify(token);
            JWTPayloadData jwtPayloadData = new JWTPayloadData(
                    decodedJWT.getSubject(),
                    List.of(decodedJWT.getClaim("role").asString())
            );
            return jwtPayloadData;
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public JWTPayloadData verifyAccessToken(String token) {
        return verifyToken(token, dotenv.get("ACCESS_TOKEN_SECRET"));
    }

    @Override
    public JWTPayloadData verifyRefreshToken(String token) {
        return verifyToken(token, dotenv.get("REFRESH_TOKEN_SECRET"));
    }
}
