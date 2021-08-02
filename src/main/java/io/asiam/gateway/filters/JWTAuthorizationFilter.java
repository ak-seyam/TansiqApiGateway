package io.asiam.gateway.filters;

import io.asiam.gateway.models.JWTPayloadData;
import io.asiam.gateway.services.JWTService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.apache.http.HttpHeaders.AUTHORIZATION;

@Slf4j
@RequiredArgsConstructor
public class JWTAuthorizationFilter extends OncePerRequestFilter {
    private final JWTService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {
        System.out.println("servlet path " + req.getServletPath());
        if (!req.getServletPath().equals("/api/login") && !req.getServletPath().equals("/api/refreshToken")) {
            String authorizationHeader = req.getHeader(AUTHORIZATION);
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                String token = authorizationHeader.substring("Bearer ".length());
                try {
                    JWTPayloadData jwtPayloadData = jwtService.verifyAccessToken(token);
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                            jwtPayloadData.getEmail(),
                            null,
                            jwtPayloadData.getRoles().stream().map(SimpleGrantedAuthority::new)
                                    .collect(Collectors.toList())
                    );
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                } catch (Exception e) {
                    log.error(e.getMessage());
                    res.setStatus(SC_UNAUTHORIZED);
                    res.setContentType(APPLICATION_JSON);
                    new ObjectMapper().writeValue(res.getOutputStream(), Map.of(
                            "success", false,
                            "message", e.getMessage()
                    ));
                }
            }
        }
        filterChain.doFilter(req, res);
    }
}
