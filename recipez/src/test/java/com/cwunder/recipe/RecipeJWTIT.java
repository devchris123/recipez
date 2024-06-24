package com.cwunder.recipe;

import org.junit.jupiter.api.*;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.MediaTypes;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import com.cwunder.recipe._test.ITAppConfig;
import com.jayway.jsonpath.JsonPath;

@Import(ITAppConfig.class)
public class RecipeJWTIT {
    private static final String baseUrl = "http://recipe-app:8080";

    private static WebTestClient testClient;

    private static String SESSION_ENDPOINT = "/sessions";
    private static String RECIPE_ENDPOINT = "/recipes";

    @BeforeAll
    static void setupAll() {
        testClient = WebTestClient.bindToServer().baseUrl(baseUrl).build();
    }

    @Test
    public void loginAndListRecipesThenOk() {
        // setup
        var rsp = new String(testClient.post().uri(SESSION_ENDPOINT)
                .headers(header -> header.setBasicAuth("user", "pass"))
                .body(BodyInserters.empty())
                .exchange()
                .expectBody().returnResult().getResponseBody());
        String tok = JsonPath.read(rsp, "$.token");

        // execute / assert
        testClient.get().uri(RECIPE_ENDPOINT)
                .accept(MediaTypes.HAL_JSON)
                .headers(header -> header.setBearerAuth(tok))
                .exchange()
                .expectStatus().isOk();
    }
}