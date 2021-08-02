package io.asiam.gateway.filters;

import io.asiam.gateway.models.JWTPayloadData;
import io.asiam.gateway.services.AccountDetails;
import io.asiam.gateway.services.JWTService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private Dotenv dotenv;
    private JWTService jwtService;
    private AccountDetails accountDetails;

    public JWTAuthenticationFilter(AccountDetails accountDetails,AuthenticationManager authenticationManager, Dotenv dotenv, JWTService jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.dotenv = dotenv;
        this.accountDetails = accountDetails;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                email,
                password
        );
        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        User user = (User) authResult.getPrincipal();
        UUID id = accountDetails.getIdByUser(user);
        String accessToken = jwtService.getAccessToken(new JWTPayloadData(user.getUsername(), id, user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList())));
        String refreshToken = jwtService.getRefreshToken(new JWTPayloadData(user.getUsername(), id, user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList())));
        response.setContentType(APPLICATION_JSON_VALUE);
        Cookie ridCookie = new Cookie("rid", refreshToken);
        ridCookie.setHttpOnly(true);
        ridCookie.setPath("/");
        ridCookie.setMaxAge(60 * 60 * 24 * 365);
        response.addCookie(ridCookie);
        new ObjectMapper().writeValue(response.getOutputStream(), Map.of("actkn", accessToken));
    }
}
