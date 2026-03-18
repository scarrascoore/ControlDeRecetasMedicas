package com.portafolio.controlrecetamedica.config;

import com.portafolio.controlrecetamedica.domain.auth.port.JwtServicePort;
import com.portafolio.controlrecetamedica.infrastructure.security.jwt.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http, JwtServicePort jwt) throws Exception {

        http
                .cors(cors -> {})
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth

                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        .requestMatchers("/health").permitAll()
                        .requestMatchers("/auth/**").permitAll()

                        .requestMatchers(HttpMethod.GET, "/specialties").authenticated()
                        .requestMatchers(HttpMethod.POST, "/specialties").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/specialties/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/specialties/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/prescriptions/").authenticated()
                        .requestMatchers(HttpMethod.POST, "/prescriptions/*").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/prescriptions/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/prescriptions/**").authenticated()

                        .requestMatchers(HttpMethod.GET, "/today").authenticated()
                        .requestMatchers(HttpMethod.POST, "/schedules/**").authenticated()
                        .requestMatchers(HttpMethod.GET, "/schedules/**").authenticated()

                        .anyRequest().authenticated()
                )
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable())
                .addFilterBefore(new JwtAuthFilter(jwt), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
