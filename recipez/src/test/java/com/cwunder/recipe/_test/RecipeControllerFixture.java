package com.cwunder.recipe._test;

import java.util.*;

import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient.BodyContentSpec;
import org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec;
import org.springframework.web.reactive.function.BodyInserters;

public class RecipeControllerFixture {
    public static Map<String, Object> createRecipeData() {
        var rec = new HashMap<String, Object>();
        rec.put("name", "recipe");
        rec.put("username", "testuser");
        return rec;
    }

    public static ResponseSpec postRecipe(WebTestClient client, Map<String, Object> recipe) {
        return client.post()
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(recipe))
                .exchange();
    }

    public static void assertIngredientQuantityJsonPath(BodyContentSpec spec) {
        spec.jsonPath("$.ingredient").exists()
                .jsonPath("$.quantity").exists()
                .jsonPath("$.unit").exists();
    }
}
