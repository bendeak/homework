package com.framework.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Reads typed test data from testdata.json on the classpath.
 * All test data is centralised in a single file.
 */
public class CredentialReader {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final String TEST_DATA_FILE = "testdata.json";

    private CredentialReader() {
    }

    public record Credentials(String username, String password) {
    }

    public record CheckoutInfo(String firstName, String lastName, String postalCode) {
    }

    public static Credentials loadUser(String userKey) {
        JsonNode node = readJson(TEST_DATA_FILE).get("users").get(userKey);
        if (node == null)
            throw new IllegalArgumentException("No user found for key: " + userKey);
        return new Credentials(
                node.get("username").asText(),
                node.get("password").asText());
    }

    public static CheckoutInfo loadCheckoutInfo() {
        JsonNode node = readJson(TEST_DATA_FILE).get("checkout");
        return new CheckoutInfo(
                node.get("firstName").asText(),
                node.get("lastName").asText(),
                node.get("postalCode").asText());
    }

    public static List<String> loadItems() {
        JsonNode array = readJson(TEST_DATA_FILE).get("items");
        List<String> items = new ArrayList<>();
        array.forEach(n -> items.add(n.asText()));
        return items;
    }

    private static JsonNode readJson(String filename) {
        try (InputStream is = CredentialReader.class
                .getClassLoader()
                .getResourceAsStream(filename)) {
            if (is == null)
                throw new IllegalArgumentException("File not found on classpath: " + filename);
            return MAPPER.readTree(is);
        } catch (Exception e) {
            throw new RuntimeException("Failed to read test data from: " + filename, e);
        }
    }
}
