package com.tests.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.framework.api.ApiClient;
import com.framework.api.ApiResponse;
import com.framework.core.BaseApiTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

// Case 5 – REST API Testing
@DisplayName("Case 5 – REST API Testing")
class UsersApiTest extends BaseApiTest {

    private static final String BASE_URL = "https://jsonplaceholder.typicode.com";
    private static final String ENDPOINT = "/users";

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    @Override
    public void setUpApi() {
        // Override the default ApiClient to point at jsonplaceholder instead of
        // the framework's configured api.base.url.
        api = new ApiClient(BASE_URL);
    }

    @Test
    @DisplayName("GET /users returns a list of users with valid email addresses")
    void getUsersReturnsUsersWithValidEmails() throws Exception {

        // Step 1 — Send GET /users
        log.info("Sending GET {}{}", BASE_URL, ENDPOINT);
        ApiResponse response = api.get(ENDPOINT);
        assertTrue(response.isSuccessful(),
                "Expected a 2xx response but got: " + response.getStatusCode());

        // Step 2 — Parse response body as JSON array
        JsonNode users = objectMapper.readTree(response.getBody());
        assertTrue(users.isArray(), "Response body should be a JSON array");
        assertFalse(users.isEmpty(), "Users array should not be empty");

        // Step 3 — Log name and email for each user
        log.info("Users from response:");
        for (JsonNode user : users) {
            String name = user.get("name").asText();
            String email = user.get("email").asText();
            log.info("{} | {}", name, email);
        }

        // Step 4 — Verify the first user's email contains @
        String firstEmail = users.get(0).get("email").asText();
        log.info("Verifying first email: '{}'", firstEmail);
        assertTrue(firstEmail.contains("@"),
                "First user's email should contain '@' but was: " + firstEmail);
    }
}
