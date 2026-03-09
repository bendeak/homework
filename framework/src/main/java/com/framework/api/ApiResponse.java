package com.framework.api;

import okhttp3.Headers;

/**
 * Immutable wrapper around an HTTP response.
 */
public class ApiResponse {

    private final int statusCode;
    private final String body;
    private final Headers headers;

    public ApiResponse(int statusCode, String body, Headers headers) {
        this.statusCode = statusCode;
        this.body       = body;
        this.headers    = headers;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getBody() {
        return body;
    }

    public String getHeader(String name) {
        return headers.get(name);
    }

    public boolean isSuccessful() {
        return statusCode >= 200 && statusCode < 300;
    }

    @Override
    public String toString() {
        return "ApiResponse{status=" + statusCode + ", body='" + body + "'}";
    }
}
