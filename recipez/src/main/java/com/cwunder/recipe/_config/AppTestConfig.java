package com.cwunder.recipe._config;

// Http
import org.springframework.http.HttpMethod;

// Annotations
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;

// Spring Security
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@Import(BaseConfig.class)
@Profile("test")
public class AppTestConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("/actuator/health").permitAll()
                        .requestMatchers(HttpMethod.POST, "/users").anonymous()
                        .anyRequest().authenticated())
                .csrf((csrf) -> csrf.disable())
                .httpBasic(withDefaults());
        return http.build();
    }
}
