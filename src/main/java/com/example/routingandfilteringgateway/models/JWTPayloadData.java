package com.example.routingandfilteringgateway.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
public class JWTPayloadData {
    private final String email;
    private final List<String> roles;
}
