package com.cwunder.recipe.user;

import java.util.*;

import org.junit.jupiter.api.*;

// DI
import org.springframework.beans.factory.annotation.Autowired;

// Http
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;

// Spring Boot Test
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

// Spring Test
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.client.MockMvcWebTestClient;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.BodyInserters;

// Recipe
import com.cwunder.recipe._test.TestFixture;
import com.cwunder.recipe._test.UserFixture;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class UserControllerTest {
    private final String CONTROLLER_URL = "/users";

    @Autowired
    TestFixture testFixture;

    @Autowired
    private UserFixture userFxt;

    private WebTestClient client;

    @BeforeEach
    void setup() {
        testFixture.cleanDB();
        // Create a user that is used in subsequent authentications
        userFxt.createUser("testuser", "testpw");
    }

    @Autowired
    void setMockMvc(MockMvc mockMvc) {
        client = MockMvcWebTestClient.bindTo(mockMvc)
                .defaultHeader("Accept", MediaTypes.HAL_JSON_VALUE)
                .build();
    }

    @Test
    void testCreateUser() {
        var user = new HashMap<String, Object>();
        user.put("username", "test");
        user.put("password", "testpw");

        // execute
        client.post().uri(CONTROLLER_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .body(BodyInserters.fromValue(user))
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaTypes.HAL_JSON)
                .expectBody()
                .jsonPath("$.publicId").exists()
                .jsonPath("$.username").isEqualTo("test");
    }
}
