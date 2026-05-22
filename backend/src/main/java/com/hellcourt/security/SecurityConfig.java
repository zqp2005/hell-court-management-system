package com.hellcourt.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.Customizer;
import org.springframework.security.web.header.writers.XXssProtectionHeaderWriter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .headers(headers -> headers
                        .xssProtection(xss -> xss.headerValue(XXssProtectionHeaderWriter.HeaderValue.ENABLED_MODE_BLOCK))
                        .contentSecurityPolicy(csp -> csp.policyDirectives("default-src 'self'"))
                        .frameOptions(fo -> fo.deny())
                        .httpStrictTransportSecurity(hsts -> hsts.includeSubDomains(true).maxAgeInSeconds(31536000)))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/login", "/doc.html",
                                "/v3/api-docs/**", "/swagger-ui/**", "/webjars/**").permitAll()
                        .requestMatchers("/api/dashboard/**", "/api/hell-layers/**", "/api/demons/**").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/souls", "/api/souls/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/souls").hasAuthority("soul:write")
                        .requestMatchers(HttpMethod.PUT, "/api/souls/*/status").hasAuthority("soul:summon")
                        .requestMatchers(HttpMethod.PUT, "/api/souls/*").hasAuthority("soul:write")
                        .requestMatchers(HttpMethod.DELETE, "/api/souls/**").hasAuthority("soul:delete")
                        .requestMatchers(HttpMethod.GET, "/api/judgments/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/judgments/*/submit").hasAuthority("judgment:submit")
                        .requestMatchers(HttpMethod.POST, "/api/judgments").hasAuthority("judgment:write")
                        .requestMatchers(HttpMethod.PUT, "/api/judgments/**").hasAuthority("judgment:write")
                        .requestMatchers(HttpMethod.GET, "/api/reviews/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/reviews").hasAuthority("review:approve")
                        .requestMatchers(HttpMethod.GET, "/api/rebirths/export").hasAuthority("user:manage")
                        .requestMatchers(HttpMethod.GET, "/api/rebirths/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/rebirths").hasAuthority("rebirth:assign")
                        .requestMatchers("/api/users/**").hasAuthority("user:manage")
                        .requestMatchers("/api/logs/**").hasAuthority("log:view")
                        .requestMatchers("/actuator/**").permitAll()
                        .anyRequest().authenticated())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }
}
