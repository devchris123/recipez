package com.cwunder.recipe._config;

// Java SE
import javax.sql.DataSource;

// Annotations
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Autowired;

// Hateoas
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.hateoas.mediatype.hal.CurieProvider;
import org.springframework.hateoas.mediatype.hal.DefaultCurieProvider;
import org.springframework.hateoas.UriTemplate;

// Spring Security
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

// Recipe
import com.cwunder.recipe.user.CustomUserDetailsService;

@Configuration
@EnableHypermediaSupport(type = { EnableHypermediaSupport.HypermediaType.HAL,
        EnableHypermediaSupport.HypermediaType.HAL_FORMS })
@EnableWebSecurity
public class BaseConfig {
    @Autowired
    DataSource dataSource;

    @Bean
    public CurieProvider curieProvider() {
        var relBaseUrl = System.getenv("REL_BASE_URL");
        var relTemplate = String.format("%s/rels/{rel}", relBaseUrl);
        return new DefaultCurieProvider("ex", UriTemplate.of(relTemplate));
    }

    @Bean
    CustomUserDetailsService customUserDetailsService() {
        return new CustomUserDetailsService();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
