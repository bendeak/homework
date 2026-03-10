package com.framework.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Reads typed test data from JSON files on the classpath.
 * Supports credentials and general key-value extraction.
 */
public class CredentialReader {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private CredentialReader() {
    }

    public record Credentials(String username, String password) {
    }

    public record CheckoutInfo(String firstName, String lastName, String postalCode) {
    }

    public static Credentials load(String filename) {
        JsonNode root = readJson(filename);
        return new Credentials(
                root.get("username").asText(),
                root.get("password").asText());
    }

    public static CheckoutInfo loadCheckoutInfo(String filename) {
        JsonNode node = readJson(filename).get("checkout");
        return new CheckoutInfo(
                node.get("firstName").asText(),
                node.get("lastName").asText(),
                node.get("postalCode").asText());
    }

    public static List<String> loadItems(String filename) {
        JsonNode array = readJson(filename).get("items");
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
