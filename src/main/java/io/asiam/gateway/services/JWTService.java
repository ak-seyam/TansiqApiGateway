package io.asiam.gateway.services;

import io.asiam.gateway.models.JWTPayloadData;

public interface JWTService {
    String getAccessToken(JWTPayloadData payload);
    String getRefreshToken(JWTPayloadData payload);
    JWTPayloadData verifyAccessToken(String token);
    JWTPayloadData verifyRefreshToken(String token);
}
