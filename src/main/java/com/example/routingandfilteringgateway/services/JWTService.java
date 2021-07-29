package com.example.routingandfilteringgateway.services;

import com.example.routingandfilteringgateway.models.JWTPayloadData;
import org.springframework.stereotype.Service;

import java.util.Map;

public interface JWTService {
    String getAccessToken(JWTPayloadData payload);
    String getRefreshToken(JWTPayloadData payload);
    JWTPayloadData verifyAccessToken(String token);
    JWTPayloadData verifyRefreshToken(String token);
}
