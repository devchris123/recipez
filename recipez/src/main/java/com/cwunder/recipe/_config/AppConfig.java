package com.cwunder.recipe._config;

// Java SE
import javax.sql.DataSource;

// Java Security
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

// Http
import org.springframework.http.HttpMethod;

// Annotations
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

// Hateoas
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.hateoas.mediatype.hal.CurieProvider;
import org.springframework.hateoas.mediatype.hal.DefaultCurieProvider;
import org.springframework.hateoas.UriTemplate;

// Spring Security
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.web.SecurityFilterChain;

// Nimbus JWT
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

// Recipe
import com.cwunder.recipe.user.CustomUserDetailsService;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@Import(BaseConfig.class)
@EnableHypermediaSupport(type = { EnableHypermediaSupport.HypermediaType.HAL,
        EnableHypermediaSupport.HypermediaType.HAL_FORMS })
@EnableWebSecurity
public class AppConfig {
    @Autowired
    DataSource dataSource;

    @Value("${jwt.public.key}")
    RSAPublicKey key;

    @Value("${jwt.private.key}")
    RSAPrivateKey priv;

    @Bean
    public CurieProvider curieProvider() {
        var relBaseUrl = System.getenv("REL_BASE_URL");
        var relTemplate = String.format("%s/rels/{rel}", relBaseUrl);
        return new DefaultCurieProvider("ex", UriTemplate.of(relTemplate));
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("/actuator/health").permitAll()
                        .requestMatchers(HttpMethod.POST, "/users").anonymous()
                        .anyRequest().authenticated())
                .csrf((csrf) -> csrf.disable())
                .oauth2ResourceServer((oauth2) -> oauth2.jwt(withDefaults()))
                .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .httpBasic(withDefaults());
        return http.build();
    }

    @Bean
    JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withPublicKey(key).build();
    }

    @Bean
    JwtEncoder jwtEncoder() {
        JWK jwk = new RSAKey.Builder(key).privateKey(priv).build();
        JWKSource<SecurityContext> jkws = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jkws);
    }

    @Bean
    CustomUserDetailsService customUserDetailsService() {
        return new CustomUserDetailsService();
    }

}
