package com.cwunder.recipe._test;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.reactive.server.WebTestClientBuilderCustomizer;
import org.springframework.boot.web.reactive.function.client.WebClientCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

@TestConfiguration
@Profile({ "test", "integration-test" })
public class ITAppConfig {
    @Bean
    public WebTestClientBuilderCustomizer webTestClientBuilderCustomizer() {
        return (builder) -> builder.baseUrl("http://recipe-app:8080")
                .defaultHeaders(header -> header.setBasicAuth("user", "pass"));
    }

    @Bean
    public WebClientCustomizer webClientCustomizer() {
        return (builder) -> builder.baseUrl("http://recipe-app:8080")
                .defaultHeaders(header -> header.setBasicAuth("user", "pass"));
    }
}
