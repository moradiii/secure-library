package com.Library.secure_library.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;

@Configuration
public class SecurityConfig {

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) {
                http
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers("/h2-console/**").permitAll()
                                                .requestMatchers("/login", "/register").permitAll()
                                                .requestMatchers("/books").hasAnyRole("USER", "ADMIN")
                                                .requestMatchers("/borrow/**").hasRole("USER")
                                                .requestMatchers("/admin/**").hasRole("ADMIN")
                                                .anyRequest().authenticated())
                                .csrf(csrf -> csrf
                                                .ignoringRequestMatchers("/h2-console/**"))
                                .headers(headers -> headers
                                                .frameOptions(frame -> frame.disable())
                                                .contentSecurityPolicy(csp -> csp
                                                                .policyDirectives(
                                                                                "default-src 'self'; script-src 'self'; style-src 'self' 'unsafe-inline'")))
                                .formLogin(form -> form
                                                .defaultSuccessUrl("/books", true)
                                                .permitAll())
                                .exceptionHandling(exception -> exception
                                                .accessDeniedPage("/403"))
                                .logout(LogoutConfigurer::permitAll);

                return http.build();
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }
}