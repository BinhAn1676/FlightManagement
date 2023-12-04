package com.binhan.flightmanagement.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private static final String[] WHITE_LIST_URL = {"/api/v1/auth/**",
            "/v2/api-docs",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui/**",
            "/webjars/**",
            "/swagger-ui.html"};
    private final JWTAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req ->
                        req.requestMatchers(WHITE_LIST_URL).permitAll()
                                .requestMatchers("/api/v1/roles").hasAnyRole("ADMIN")
                                .requestMatchers("/api/v1/admin/**").hasAnyRole("ADMIN")
                                .requestMatchers(POST,"/api/v1/flight/**").hasAnyRole("ADMIN")
                                .requestMatchers(PUT,"/api/v1/flight/**").hasAnyRole("ADMIN")
                                .requestMatchers(DELETE,"/api/v1/flight/**").hasAnyRole("ADMIN")
                                .requestMatchers(POST,"/api/v1/country/**").hasAnyRole("ADMIN")
                                .requestMatchers(PUT,"/api/v1/country/**").hasAnyRole("ADMIN")
                                .requestMatchers(DELETE,"/api/v1/country/**").hasAnyRole("ADMIN")
                                .requestMatchers(POST,"/api/v1/airport/**").hasAnyRole("ADMIN")
                                .requestMatchers(PUT,"/api/v1/airport/**").hasAnyRole("ADMIN")
                                .requestMatchers(DELETE,"/api/v1/airport/**").hasAnyRole("ADMIN")
                                .requestMatchers(POST,"/api/v1/aircraft/**").hasAnyRole("ADMIN")
                                .requestMatchers(PUT,"/api/v1/aircraft/**").hasAnyRole("ADMIN")
                                .requestMatchers(DELETE,"/api/v1/aircraft/**").hasAnyRole("ADMIN")
                                .requestMatchers("/api/v1/payment/**").authenticated()
                                .requestMatchers("/api/v1/reservation/**").authenticated()
                                .requestMatchers("/api/v1/users/**").authenticated()
                                .anyRequest()
                                .permitAll()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

}