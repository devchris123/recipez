package com.cwunder.recipe._config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.hateoas.mediatype.hal.CurieProvider;
import org.springframework.hateoas.mediatype.hal.DefaultCurieProvider;
import org.springframework.hateoas.UriTemplate;

@Configuration
@EnableHypermediaSupport(type = { EnableHypermediaSupport.HypermediaType.HAL,
        EnableHypermediaSupport.HypermediaType.HAL_FORMS })
public class CuryConfig {

    @Bean
    public CurieProvider curieProvider() {
        var relBaseUrl = System.getenv("REL_BASE_URL");
        var relTemplate = String.format("%s/rels/{rel}", relBaseUrl);
        return new DefaultCurieProvider("ex", UriTemplate.of(relTemplate));
    }
}
