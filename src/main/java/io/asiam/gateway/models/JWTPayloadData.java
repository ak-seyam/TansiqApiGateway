package io.asiam.gateway.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@RequiredArgsConstructor
public class JWTPayloadData {
    private final String email;
    private final UUID id;
    private final List<String> roles;
}
