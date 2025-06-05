// src/main/java/com/example/backend/config/SecurityConfig.java
package com.example.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        // parametrul implicit strength=10 este suficient pentru majoritatea cazurilor
        return new BCryptPasswordEncoder();
    }
}
