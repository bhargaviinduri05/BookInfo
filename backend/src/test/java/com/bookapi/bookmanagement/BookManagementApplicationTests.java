package com.bookapi.bookmanagement;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * Basic smoke test to verify the Spring application context loads successfully.
 * Note: requires a reachable PostgreSQL instance as configured in application.properties,
 * or an in-memory/test profile override.
 */
@SpringBootTest
class BookManagementApplicationTests {

    @Test
    void contextLoads() {
        // If the application context fails to start, this test will fail.
    }
}
