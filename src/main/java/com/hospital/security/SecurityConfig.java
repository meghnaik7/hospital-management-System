package com.hospital.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Central Spring Security configuration.
 *
 * Role permissions:
 *  - /api/auth/**          → public (login / register)
 *  - /h2-console/**        → public (dev convenience)
 *  - GET /api/doctors/**   → ADMIN, DOCTOR, PATIENT (anyone authenticated)
 *  - POST/PUT/DELETE doctors → ADMIN only
 *  - GET /api/patients/**  → ADMIN, DOCTOR
 *  - CRUD patients         → ADMIN
 *  - /api/appointments/**  → ADMIN, DOCTOR, PATIENT (scoped by service logic)
 *  - /api/admin/**         → ADMIN only
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity          // allows @PreAuthorize on individual methods
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Disable CSRF – not needed for stateless REST APIs
            .csrf(AbstractHttpConfigurer::disable)

            // Allow H2 console frames (dev only)
            .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()))

            .authorizeHttpRequests(auth -> auth
                // Public endpoints
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/h2-console/**").permitAll()

                // Admin only
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/doctors/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/patients/**").hasRole("ADMIN")

                // Doctors: ADMIN or DOCTOR can create/update doctor profiles
                .requestMatchers(HttpMethod.POST, "/api/doctors/**").hasAnyRole("ADMIN", "DOCTOR")
                .requestMatchers(HttpMethod.PUT,  "/api/doctors/**").hasAnyRole("ADMIN", "DOCTOR")

                // Patients: ADMIN or PATIENT can create/update patient profiles
                .requestMatchers(HttpMethod.POST, "/api/patients/**").hasAnyRole("ADMIN", "PATIENT")
                .requestMatchers(HttpMethod.PUT,  "/api/patients/**").hasAnyRole("ADMIN", "PATIENT")

                // All other requests require authentication
                .anyRequest().authenticated()
            )

            // Stateless session – no HTTP session, token per request
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )

            .authenticationProvider(authenticationProvider())

            // Our JWT filter runs before Spring's username/password filter
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
