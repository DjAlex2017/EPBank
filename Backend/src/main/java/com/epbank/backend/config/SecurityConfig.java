package com.epbank.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/* Annotation Definitions
@Configuration - This class contains configuration for my application.
@Bean - Tells Spring create a PasswordEncoder object and keep it available for other classes that need it.
 */
@Configuration 
public class SecurityConfig {
    
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
