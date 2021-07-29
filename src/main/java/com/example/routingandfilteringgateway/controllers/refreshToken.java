package com.example.routingandfilteringgateway.controllers;

import com.example.routingandfilteringgateway.models.JWTPayloadData;
import com.example.routingandfilteringgateway.services.AccountDetails;
import com.example.routingandfilteringgateway.services.JWTService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class refreshToken {
    private final JWTService jwtService;
    private final AccountDetails accountDetails;

    @GetMapping("/api/refreshToken")
    public ResponseEntity<Map<String, Object>> getRefreshedToken(@CookieValue("rid") String refreshToken, HttpServletRequest req) {
        JWTPayloadData payloadData = jwtService.verifyRefreshToken(refreshToken);
        if (payloadData != null) {
            String email = payloadData.getEmail();
            UserDetails userDetails = accountDetails.loadUserByUsername(email);
            List<String> roles = userDetails.getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());
            String accessToken = jwtService.getAccessToken(new JWTPayloadData(email, roles));
            return new ResponseEntity<>(
                    Map.of("success", true, "actkn", accessToken),
                    new HttpHeaders(),
                    HttpStatus.OK
            );
        } else {
            return new ResponseEntity<>(
                    Map.of("success", false, "message", "invalid refresh token"),
                    new HttpHeaders(),
                    HttpStatus.FORBIDDEN
            );
        }
    }

}
