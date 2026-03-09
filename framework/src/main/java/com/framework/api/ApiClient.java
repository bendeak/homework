package com.framework.api;

import com.framework.config.FrameworkConfig;
import okhttp3.*;
import okhttp3.logging.HttpLoggingInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Reusable REST API client built on OkHttp3.
 * Instantiate per test or share as a singleton per suite.
 */
public class ApiClient {

    private static final Logger log = LoggerFactory.getLogger(ApiClient.class);
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    private final OkHttpClient httpClient;
    private final String baseUrl;

    public ApiClient() {
        this(FrameworkConfig.apiBaseUrl());
    }

    public ApiClient(String baseUrl) {
        this.baseUrl = baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;
        this.httpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .addInterceptor(new HttpLoggingInterceptor(log::debug)
                        .setLevel(HttpLoggingInterceptor.Level.BASIC))
                .build();
    }

    // --- GET ---

    public ApiResponse get(String path) {
        return get(path, Map.of());
    }

    public ApiResponse get(String path, Map<String, String> headers) {
        Request.Builder reqBuilder = new Request.Builder().url(baseUrl + path).get();
        headers.forEach(reqBuilder::addHeader);
        return execute(reqBuilder.build());
    }

    // --- POST ---

    public ApiResponse post(String path, String jsonBody) {
        return post(path, jsonBody, Map.of());
    }

    public ApiResponse post(String path, String jsonBody, Map<String, String> headers) {
        RequestBody body = RequestBody.create(jsonBody, JSON);
        Request.Builder reqBuilder = new Request.Builder().url(baseUrl + path).post(body);
        headers.forEach(reqBuilder::addHeader);
        return execute(reqBuilder.build());
    }

    // --- PUT ---

    public ApiResponse put(String path, String jsonBody) {
        RequestBody body = RequestBody.create(jsonBody, JSON);
        Request request = new Request.Builder().url(baseUrl + path).put(body).build();
        return execute(request);
    }

    // --- DELETE ---

    public ApiResponse delete(String path) {
        Request request = new Request.Builder().url(baseUrl + path).delete().build();
        return execute(request);
    }

    // --- Core executor ---

    private ApiResponse execute(Request request) {
        log.info("{} {}", request.method(), request.url());
        try (Response response = httpClient.newCall(request).execute()) {
            String bodyStr = response.body() != null ? response.body().string() : "";
            log.debug("Response [{}]: {}", response.code(), bodyStr);
            return new ApiResponse(response.code(), bodyStr, response.headers());
        } catch (IOException e) {
            log.error("Request failed: {}", e.getMessage(), e);
            throw new RuntimeException("HTTP request failed", e);
        }
    }
}
