package io.asiam.gateway.config;

import io.asiam.gateway.filters.JWTAuthenticationFilter;
import io.asiam.gateway.filters.JWTAuthorizationFilter;
import io.asiam.gateway.services.AccountDetails;
import io.asiam.gateway.services.JWTService;
import io.github.cdimascio.dotenv.Dotenv;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import javax.servlet.http.Cookie;

import static org.springframework.http.HttpMethod.*;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final Dotenv dotenv;
    private final JWTService jwtService;
    private final AccountDetails accountDetails;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        JWTAuthenticationFilter jwtAuthenticationFilter = new JWTAuthenticationFilter(accountDetails, authenticationManagerBean(), dotenv, jwtService);
        jwtAuthenticationFilter.setFilterProcessesUrl("/api/login");
        http.csrf().disable();
        http.cors();
        http.logout().logoutUrl("/api/logout").addLogoutHandler((req, res, auth) -> {
            for (var c : req.getCookies()) {
                String cookieName = c.getName();
                Cookie cookieDeleted = new Cookie(cookieName, null);
                cookieDeleted.setMaxAge(0);
                cookieDeleted.setPath("/");
                cookieDeleted.setHttpOnly(true);
                res.addCookie(cookieDeleted);
            }
        }).logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler(HttpStatus.NO_CONTENT));
        http.sessionManagement().sessionCreationPolicy(STATELESS);
        http.authorizeRequests().antMatchers("/api/login/**").permitAll();
        http.authorizeRequests().antMatchers("/api/refreshToken/**").permitAll();
        http.authorizeRequests().antMatchers(GET,"/api/core/tansiq/**").permitAll();
        http.authorizeRequests().antMatchers("/api/core/majors").permitAll();
        http.authorizeRequests().antMatchers(DELETE,"/api/core/tansiq/**").hasAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers(POST, "/api/core/studentFiles/**").hasAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers(GET, "/api/core/admins/**").hasAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers("/api/core/students/**").hasAuthority("ROLE_STUDENT");
        http.addFilter(jwtAuthenticationFilter);
        http.addFilterBefore(new JWTAuthorizationFilter(jwtService), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
