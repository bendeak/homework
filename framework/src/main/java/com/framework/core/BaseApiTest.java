package com.framework.core;

import com.framework.api.ApiClient;
import org.junit.jupiter.api.BeforeEach;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseApiTest {

    protected ApiClient api;
    protected final Logger log = LoggerFactory.getLogger(getClass());

    @BeforeEach
    public void setUpApi() {
        log.info("=== Starting API test: {} ===", getClass().getSimpleName());
        api = new ApiClient();
    }
}
