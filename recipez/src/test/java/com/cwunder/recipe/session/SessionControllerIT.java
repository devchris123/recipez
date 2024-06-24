package com.cwunder.recipe.session;

import org.junit.jupiter.api.*;

// DI
import org.springframework.beans.factory.annotation.Autowired;

// Http
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;

// Spring Boot Test
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

// Spring test
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.client.MockMvcWebTestClient;

import org.springframework.transaction.annotation.Transactional;

// Recipe
import com.cwunder.recipe._test.TestFixture;
import com.cwunder.recipe._test.UserFixture;
import com.cwunder.recipe._test.WithMockCustomUser;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles({ "integration-test" })
public class SessionControllerIT {
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
    @WithMockCustomUser(username = "testuser", password = "testpw")
    void testCreateSession() {
        // setup
        client.post().uri("/sessions")
                .contentType(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.token").exists();
    }
}
